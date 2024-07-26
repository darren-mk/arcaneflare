(ns arcaneflare.database.email-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.api :as a]
   [arcaneflare.database.email :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def id
  #uuid "c591d52f-22d0-45b6-9fa1-2b9a0b93d641")

(def address
  "ringo@eml.com")

(def email
  {:xt/id id
   :email/address address
   :email/verified? false})

(t/deftest integrate-test
  (with-open [node (a/->node {})]
    (a/atx node (sut/create! node email))
    (let [db (a/->db node)]
      (t/is (= email (a/ent db id)))
      (t/is (false? (sut/new-email-address-avail?
                     db address))))))
