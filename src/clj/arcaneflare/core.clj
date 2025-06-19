(ns arcaneflare.core
  (:require
   [arcaneflare.database.base]
   [arcaneflare.handler]
   [mount.core :as m]))

(defn -main []
  (doseq [component (:started (m/start))]
    (println "started: " component))
  (println "backend successfully started"))