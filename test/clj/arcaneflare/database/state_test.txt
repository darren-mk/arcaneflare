(ns arcaneflare.database.state-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.country :as db-country]
   [arcaneflare.database.state :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def country-id
  #uuid "b7f95995-e2c8-4012-b0a0-99cd6d956403")

(def country
  {:xt/id country-id
   :country/label "United States"
   :country/acronym "US"})

(def ny
  {:xt/id #uuid "fd14d062-bbac-4d8c-b5de-4a83a2481c25"
   :state/label "New York"
   :state/acronym "NY"
   :state/country-id country-id})

(def nj
  {:xt/id #uuid "1a02fbd6-be1c-4fe6-b21e-89299bb26ca6"
   :state/label "New Jersey"
   :state/acronym "NJ"
   :state/country-id country-id})

(t/deftest integrate-test
  (with-open [node (i/->node {})]
    (i/atx node (db-country/create! node country))
    (i/atx node (sut/create! node ny))
    (i/atx node (sut/create! node nj))
    (let [db (i/->db node)]
      (t/is (= #{ny nj} (set (sut/states-in db country-id)))))))
