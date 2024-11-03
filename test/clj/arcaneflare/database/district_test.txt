(ns arcaneflare.database.district-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.country :as db-country]
   [arcaneflare.database.state :as db-state]
   [arcaneflare.database.district :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def country-id
  #uuid "b7f95995-e2c8-4012-b0a0-99cd6d956403")

(def country
  {:xt/id country-id
   :country/label "United States"
   :country/acronym "US"})

(def state-id
  #uuid "6ea3758d-34c4-4545-8337-d07cca133a5d")

(def state
  {:xt/id state-id
   :state/label "New York"
   :state/acronym "NY"
   :state/country-id country-id})

(def queens
  {:xt/id #uuid "a4cdc4a1-fc00-44ff-af5b-4ae1ecbb7b8e"
   :district/label "Queens"
   :district/state-id state-id})

(def bronx
  {:xt/id #uuid "e1f4e136-aab1-4719-b880-696e75d0032b"
   :district/label "Bronx"
   :district/state-id state-id})

(t/deftest districts-in-test
  (with-open [node (i/->node {})]
    (i/atx node (db-country/create! node country))
    (i/atx node (db-state/create! node state))
    (i/atx node (sut/create! node queens))
    (i/atx node (sut/create! node bronx))
    (let [db (i/->db node)]
      (t/is (= #{queens bronx} (set (sut/districts-in db state-id)))))))
