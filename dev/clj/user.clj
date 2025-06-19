(ns user
  (:require
   [clojure.tools.namespace.repl :as tn]
   [mount.core :as m]
   [arcaneflare.database.base]
   [arcaneflare.handler]
   [arcaneflare.database.migration :as migration]
   [arcaneflare.database.geo.root :as geo.root]
   [arcaneflare.database.place.root :as place.root]))

(def create-migration
  #'migration/create)

(def run-migration
  #'migration/run)

(def rollback-migration
  #'migration/rollback)

(defn start []
  (m/start))

(defn stop []
  (m/stop))

(defn restart []
  (tn/refresh-all)
  (stop)
  (start))

(defn seed []
  (geo.root/seed!)
  (place.root/seed!)
  (println "successfully seeded"))

(comment
  (create-migration)
  (run-migration)
  (rollback-migration)
  (start)
  (stop)
  (restart)
  (seed))
