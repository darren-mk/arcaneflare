(ns arcaneflare.core
  (:require
   [integrant.core :as ig]
   [ring.adapter.jetty :as rj]
   [reitit.ring :as rr]
   [taoensso.timbre :as log]
   [arcaneflare.database.core :as dbc]))

(def config
  {::dbc/database {:dbtype "postgres"
                   :dbname "arcaneflare"
                   :user "dev"
                   :password "abc"}
   ::handler {:database (ig/ref ::dbc/database)}
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

(defmethod ig/init-key ::handler [_ _]
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
