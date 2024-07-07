(ns arcaneflare.core
  (:require
   [integrant.core :as ig]
   [ring.adapter.jetty :as rj]
   [reitit.ring :as rr]
   [taoensso.timbre :as log]
   [arcaneflare.database.core :as dbc]
   [arcaneflare.database.migrate :as dbm]
   [arcaneflare.database.person :as db-person]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.interface.eql :as p.eql]))

(def temperatures
  {"Recife" 23})

(pco/defresolver temperature-from-city
  [{:keys [city]}]
  {:temperature (get temperatures city)})

(pco/defresolver cold?
  [{:keys [temperature]}]
  {:cold? (< temperature 20)})

(pco/defresolver person-by-email
  [{:keys [email]}]
  {:person (db-person/db-get-one-by-email email)})

(def indexes
  (pci/register
   [temperature-from-city
    cold?
    person-by-email]))

(def eql-process
  p.eql/process)

(comment
  (eql-process
   indexes
   {:email "kokonut@abc.com"}
   [:person])
  :=> {:person
       #:person{:id #uuid "da3c8e57-955c-43d3-b60e-bdd1f339e853",
                :username "kokonut",
                :email "kokonut@abc.com",
                :job "owner",
                :verified false,
                :created_at #inst "2024-07-04T21:51:28.723000000-00:00",
                :edited_at #inst "2024-07-04T21:51:28.723000000-00:00"}})

(def config
  {::dbc/spec {:dbtype "postgres"
               :dbname "arcaneflare"
               :user "dev"
               :password "abc"}
   ::dbm/migration {:store :database
                    :migration-dir "migrations"
                    :init-in-transaction? false
                    :migration-table-name "migrations"
                    :dbspec (ig/ref ::dbc/spec)}
   ::dbc/database {:dbspec (ig/ref ::dbc/spec)}
   ::handler {}
   ::server {:port 3000
             :join? false
             :handler (ig/ref ::handler)}})

(defn ping-handler [_req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn echo-handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str req)})

(defmethod ig/init-key ::handler
  [_ _]
  (log/info "handler started on router")
  (rr/ring-handler
   (rr/router
    ["/api"
     ["/ping" {:get ping-handler}]
     ["/echo" {:get echo-handler}]])))

(defmethod ig/init-key ::server
  [_ {:keys [port join? handler]}]
  (log/info "server started on port" port)
  (rj/run-jetty handler {:port port :join? join?}))

(defmethod ig/halt-key! ::server
  [_ server]
  (log/info "server stopped")
  (.stop server))

(defn ^:export run [_]
  (ig/init config))
