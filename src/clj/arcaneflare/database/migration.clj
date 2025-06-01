(ns arcaneflare.database.migration
  (:require
   [honey.sql :as sql]
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

(defn full-list []
  (let [q {:select [:*]
           :from [:migration]
           :order-by [:applied]}]
    (db-base/exc (honey.sql/format q))))

(defn most-recent []
  (last (full-list)))

(comment
  (create "create-user-table")
  (run)
  (rollback)
  (up 20111206154000)
  (down 20111206154000)
  (full-list))

(most-recent)
