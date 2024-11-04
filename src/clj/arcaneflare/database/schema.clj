(ns arcaneflare.database.schema)

(defonce person
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

(defonce root
  (concat person))
