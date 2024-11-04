(ns arcaneflare.database.person
  (:require
   [datomic.client.api :as d]))

(def find-email-qr
    '[:find (pull ?person [*])
      :in $ ?email
      :where [?person :person/email ?email]])

(defn find-all-by-email [db email]
  (first (d/q find-email-qr db email)))

(defn find-one-by-email [db email]
  (ffirst (d/q find-email-qr db email)))

(defn new-email-avail?
  "TODO: use datomic function for counting"
  [db email]
  (-> (find-all-by-email db email)
      count zero?))

(defn create! [conn person]
  (d/transact
   conn {:tx-data [person]}))
