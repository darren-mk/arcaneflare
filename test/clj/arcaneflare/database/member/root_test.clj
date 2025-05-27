(ns arcaneflare.database.member.root-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.member.root :as a]))

(t/deftest cycle-test
  (let [id #uuid "3d8e4a6b-38d6-4d85-9387-0da5cac56f8e"
        username "masterplan-test"
        email "masterplan-test@eml.com"
        role "customer"
        passcode "fakepasscode"
        m #:member{:id id :username username
                   :email email :role role
                   :passcode passcode}]
    (a/insert! m)
    (t/are [field-m]
           (map? (a/get-by field-m))
      {:member/id id}
      {:member/username username}
      {:member/email email})
    (t/is (map? (a/authenticate username passcode)))
    (a/update-last-login! {:member/id id})
    (println (inst? (get (a/get-by {:member/id id})
                         :member/last-login)))
    (a/remove! {:member/id id})
    (t/is (nil? (a/get-by {:member/id id})))))