(ns user
  (:require
   [clojure.tools.namespace.repl :as tn]
   [mount.core :as m]
   [clojure.spec.alpha :as s]
   [orchestra.spec.test :as ost]
   [arcaneflare.core :as c]
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
  (s/check-asserts true)
  (ost/instrument)
  (m/start #'c/server))

(defn stop []
  (s/check-asserts false)
  (ost/unstrument)
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
