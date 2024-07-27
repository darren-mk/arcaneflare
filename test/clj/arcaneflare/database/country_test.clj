(ns arcaneflare.database.country-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.country :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def id
  #uuid "b7f95995-e2c8-4012-b0a0-99cd6d956403")

(def country
  {:xt/id id
   :country/label "United States"
   :country/acronym "US"})

(t/deftest integrate-test
  (with-open [node (i/->node {})]
    (i/atx node (sut/create! node country))
    (let [db (i/->db node)]
      (t/is (= country (i/ent db id))))))
