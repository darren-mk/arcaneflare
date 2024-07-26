(ns arcaneflare.database.email-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as i]
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
  (with-open [node (i/->node {})]
    (i/atx node (sut/create! node email))
    (let [db (i/->db node)]
      (t/is (= email (i/ent db id)))
      (t/is (false? (sut/new-email-address-avail?
                     db address))))))
