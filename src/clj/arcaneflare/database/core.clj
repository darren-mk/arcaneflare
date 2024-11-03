(ns arcaneflare.database.core
  (:require
   [datomic.client.api :as d]))

(def schema
  [{:db/ident :movie/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The title of the movie"}
   {:db/ident :movie/genre
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The genre of the movie"}
   {:db/ident :movie/release-year
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "The year the movie was released in theaters"}])

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
 conn {:tx-data schema})

(defn ->db []
  (d/db conn)) 

(comment
  (d/list-databases client {})
  :=> '("main")
  (->db))
