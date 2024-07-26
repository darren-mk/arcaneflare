(ns arcaneflare.database.person-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.email :as eml]
   [arcaneflare.database.person :as sut]
   [orchestra.spec.test :as ost]))

(s/check-asserts true)
(ost/instrument 'sut)

(def email-address
  "ringo@eml.com")

(def email
  {:xt/id #uuid "c591d52f-22d0-45b6-9fa1-2b9a0b93d641"
   :email/address email-address
   :email/verified? false})

(def id
  #uuid "6673c394-f674-4601-b587-415f50b07fea")

(def username
  "ringo")

(def person
  {:xt/id id
   :person/username username
   :person/email-id #uuid "c591d52f-22d0-45b6-9fa1-2b9a0b93d641"
   :person/role :role/customer})

(t/deftest integrate-test
  (with-open [node (i/->node {})]
    (i/atx node (sut/create! node person))
    (i/atx node (eml/create! node email))
    (let [db (i/->db node)]
      (t/is (= person (i/ent db id)))
      (t/is (false? (sut/new-username-avail? db username)))
      (t/is (= 1 (sut/count-people db)))
      (t/is (= person (sut/find-by-email-address db email-address))))))
