(ns arcaneflare.database.base-test
   (:require
    [arcaneflare.database.base :as sut]
    [datomic.client.api :as d]))

(def test-db-info
  {:db-name "test"})

(defn conn []
  (d/delete-database sut/client test-db-info)
  (d/create-database sut/client test-db-info)
  (let [c (d/connect sut/client test-db-info)]
    (d/transact c {:tx-data sut/schema})
    c))

(defn ->db [conn]
  (d/db conn))
