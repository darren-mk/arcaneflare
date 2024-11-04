(ns arcaneflare.database.base
  (:require
   [arcaneflare.database.schema :as schema]
   [datomic.client.api :as d]))

(defonce system
  "arcaneflare")

(defonce cfg
  {:server-type :datomic-local
   :storage-dir :mem
   :system system})

(defonce client
  (d/client cfg))

(defonce db-name
  "main")

(defonce db-info
  {:db-name db-name})

(d/create-database
 client db-info)

(def conn
  (d/connect
   client db-info))

(d/transact
 conn {:tx-data schema/root})

(defn ->db []
  (d/db conn)) 

(comment
  (d/list-databases client {})
  :=> '("main")
  (->db))
