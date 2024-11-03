(ns arcaneflare.database.city-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.country :as db-country]
   [arcaneflare.database.state :as db-state]
   [arcaneflare.database.district :as db-district]
   [arcaneflare.database.city :as sut]))

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

(def district-id
  #uuid "a4cdc4a1-fc00-44ff-af5b-4ae1ecbb7b8e")

(def district
  {:xt/id district-id
   :district/label "Queens"
   :district/state-id state-id})

(def astoria
  {:xt/id #uuid "5cf603a5-9243-44a8-8651-9b9498519dfd"
   :city/label "Astoria"
   :city/district-id district-id})

(def flushing
  {:xt/id #uuid "e8cd287c-92dc-40d4-a269-203aabebe183"
   :city/label "Flushing"
   :city/district-id district-id})

(t/deftest districts-in-test
  (with-open [node (i/->node {})]
    (i/atx node (db-country/create! node country))
    (i/atx node (db-state/create! node state))
    (i/atx node (db-district/create! node district))
    (i/atx node (sut/create! node astoria))
    (i/atx node (sut/create! node flushing))
    (let [db (i/->db node)]
      (t/is (= 5 (i/count-by-k db :xt/id)))
      (t/is (= #{astoria flushing}
               (set (sut/cities-in db district-id)))))))
