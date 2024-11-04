(ns arcaneflare.database.base
  (:require
   [datomic.client.api :as d]))

(defonce schema
  [{:db/ident :person/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :person/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :person/username
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :person/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :person/role
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one}])

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
