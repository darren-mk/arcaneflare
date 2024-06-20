(ns tia.core
  (:require
   [integrant.core :as ig]
   [ring.adapter.jetty :as rj]))

(def config
  {:adapter/jetty {:port 3000
                   :join? false}})

(defn handler [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defmethod ig/init-key :adapter/jetty
  [_ {:keys [port] :as opts}]
  (println "jetty starts on port" port)
  (rj/run-jetty handler opts))

(defmethod ig/halt-key! :adapter/jetty
  [_ server]
  (.stop server))

(defn ^:export run [_]
  (ig/init config))