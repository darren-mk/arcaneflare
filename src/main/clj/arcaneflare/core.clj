(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.interface.eql :as p.eql]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.database.person :as db-person]))

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
  {:person (db-person/find-by-email email)})

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

(defn ping [_req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "pong"})

( defn echo [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str req)})

(defn frontend [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (slurp (io/resource "public/index.html"))})

(def router
  (rmd/wrap-defaults
   (rr/ring-handler
    (rr/router
     [["/" {:get frontend}]
      ["/api"
       ["/ping" {:get ping}]
       ["/echo" {:get echo}]]])
    (rr/routes
     (rr/create-resource-handler {:path "/"})
     (rr/create-default-handler)))
   rmd/site-defaults))

(declare server)

(m/defstate server
  :start (rj/run-jetty
          router
          {:port 3000 :join? false})
  :stop (.stop server))

(defn ^:export run [_]
  (m/start))
