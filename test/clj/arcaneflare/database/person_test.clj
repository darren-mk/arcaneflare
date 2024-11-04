(ns arcaneflare.database.person-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.base-test :as bt]
   [arcaneflare.database.person :as sut]))

(def id
  #uuid "9cd7dd5d-d699-4303-b31a-4a778b66cdfb")

(def email
  "ringo@eml.com")

(def person
   {:person/id id
    :person/username "unagi"
    :person/email email
    :person/role :role/customer
    :person/password "Zxc123!@#"})

(t/deftest integrate-test
  (let [tconn (bt/conn)]
    (sut/create! tconn person)
    (let [tdb (bt/->db tconn)
          person-db (sut/find-one-by-email tdb email)]
      (t/is (= person (dissoc person-db :db/id)))
      (t/is (true? (sut/new-email-avail? tdb "mccartney@eml.com")))
      (t/is (false? (sut/new-email-avail? tdb email))))))
