(ns arcaneflare.core-test
  (:require
   #_#_#_
   [clojure.test :as t]
   [arcaneflare.core :as sut]
   [arcaneflare.database.person :as db-person]))
#_#_
(def sample-person
  #:person
  {:id #uuid "da3c8e57-955c-43d3-b60e-bdd1f339e853"
   :username "kokonut"
   :email "kokonut@abc.com"
   :job "owner"
   :verified false
   :created_at #inst "2024-07-04T21:51:28.723000000-00:00"
   :edited_at #inst "2024-07-04T21:51:28.723000000-00:00"})

(t/deftest indexes-test
  (with-redefs [db-person/db-get-one-by-email
                (constantly sample-person)]
    (t/is (= sample-person
             (:person (sut/eql-process
                       sut/indexes
                       {:email "kokonut@abc.com"}
                       [:person]))))))
