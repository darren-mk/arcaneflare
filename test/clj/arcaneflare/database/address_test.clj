(ns arcaneflare.database.address-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.country :as db-country]
   [arcaneflare.database.state :as db-state]
   [arcaneflare.database.district :as db-district]
   [arcaneflare.database.city :as db-city]
   [arcaneflare.database.address :as sut]))

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

(def city-id
  #uuid "5cf603a5-9243-44a8-8651-9b9498519dfd")

(def city
  {:xt/id city-id
   :city/label "Astoria"
   :city/district-id district-id})

(def meca-ave-30
  {:xt/id #uuid "e8cd287c-92dc-40d4-a269-203aabebe183"
   :address/street "30 Meca Ave"
   :address/zip "13492"
   :address/city-id city-id})

(def dump-st-17
  {:xt/id #uuid "a3d98813-dc7f-47bc-87f8-d0e8a49b372c"
   :address/street "17 Dump Street"
   :address/zip "13425"
   :address/city-id city-id})

(t/deftest districts-in-test
  (with-open [node (i/->node {})]
    (i/atx node (db-country/create! node country))
    (i/atx node (db-state/create! node state))
    (i/atx node (db-district/create! node district))
    (i/atx node (db-city/create! node city))
    (i/atx node (sut/create! node meca-ave-30))
    (i/atx node (sut/create! node dump-st-17))
    (let [db (i/->db node)]
      (t/is (= 6 (i/count-by-k db :xt/id)))
      (t/is (= #{meca-ave-30 dump-st-17}
               (set (sut/addresses-in db city-id)))))))
