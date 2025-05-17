(ns arcaneflare.database.migration
  (:require
   [migratus.core :as migratus]
   [arcaneflare.database.base :as db-base]))

(def config {:store :database
             :migration-dir "migrations/"
             :migration-table-name "migration"
             :db db-base/db})

(defn create [label]
  (migratus/create
   config label))

(defn run []
  (migratus/migrate config))

(defn rollback []
  (migratus/rollback config))

(defn up [tag]
  (migratus/up
   config tag))

(defn down [tag]
  (migratus/down
   config tag))

(comment
  (create "create-user-table")
  (run)
  (rollback)
  (up 20111206154000)
  (down 20111206154000))