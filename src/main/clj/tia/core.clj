(ns tia.core
  (:require
   [integrant.core :as ig]
   [ring.adapter.jetty :as rj]
   [reitit.ring :as rr]))

(def config
  {::handler {}
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
  (rr/ring-handler
   (rr/router
    ["/api"
     ["/ping" {:get ping-handler}]
     ["/echo" {:get echo-handler}]])))

(defmethod ig/init-key ::server
  [_ {:keys [port join? handler]}]
  (println "server running in port" port)
  (rj/run-jetty handler {:port port :join? join?}))

(defmethod ig/halt-key! ::server
  [_ server]
  (.stop server))

(defn ^:export run [_]
  (ig/init config))