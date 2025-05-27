(ns arcaneflare.database.member.performer-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.member.performer :as a]
   [arcaneflare.database.member.root :as root]))

(t/deftest cycle-test
  (let [id #uuid "60b53f76-c057-428e-a0df-9861745d1cf6"
        username "masterplan-test"
        email "masterplan-test@eml.com"
        role "customer"
        passcode "fakepasscode"
        m #:member{:id id :username username
                   :email email :role role
                   :passcode passcode}]
    (root/insert! m)
    (t/are [field-m]
           (map? (root/get-by field-m))
      {:member/id id}
      {:member/username username}
      {:member/email email})
    (a/upsert!
     {:member/id id
      :performer/display-name "master-on-stage"
      :performer/bio "i am a star"})
    (t/is (map? (a/get-by {:member/id id})))
    (a/remove! {:member/id id})
    (t/is (nil? (a/get-by {:member/id id})))
    (root/remove! {:member/id id})
    (t/is (nil? (root/get-by {:member/id id})))))