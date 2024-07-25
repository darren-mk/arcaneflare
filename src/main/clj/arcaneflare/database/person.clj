(ns arcaneflare.database.person
  (:require
   [arcaneflare.common.schema]
   [arcaneflare.database.common :as dbcm]
   [clojure.spec.alpha :as s]))

(defn nickname-existent? [s]
  (pos? (dbcm/count-all-having-kv
         :person/nickname s)))

(comment
  (nickname-existent? "abc")
  :=> false)

(defn email-existent? [s]
  (pos? (dbcm/count-all-having-kv
         :person/email s)))

(comment
  (email-existent? "darren@email.com")
  :=> false)

(s/fdef create!
  :args (s/cat :person :person/object)
  :ret any?)

(defn create! [person]
  (s/assert :person/object person)
  (dbcm/put! person))

(comment
  (create! {:xt/id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e"
            :person/nickname "jackiemema"
            :person/email "jackie@abc.com"
            :person/password "Abc123!@#"
            :person/role (keyword "customer")
            :person/agreed? (= "on" "on")})
  :=> #:xtdb.api{:tx-id 498,
                 :tx-time #inst "2024-01-20T20:03:50.401-00:00"})

(defn count-persons []
  (dbcm/count-all-having-key :person/email))

(comment
  (count-persons)
  :=> 2)

#_
(s/fdef find-by-email
  :args (s/cat :email :person/email)
  :ret :person/object)

(defn find-by-email [email]
   {:person/id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
       :person/nickname "kokonut",
       :person/email email
       :person/password "Abc123!@#",
       :person/role :customer,
       :person/agreed? true,
       :xt/id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e"}
  #_
  (ffirst
   (dbcm/query
    '{:find [(pull ?person [*])]
      :in [[?email]]
      :where [[?person :person/email ?email]]}
    [email])))

(comment
  (find-by-email "kokonut@abc.com")
  :=> {:person/id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
       :person/nickname "kokonut",
       :person/email "kokonut@abc.com",
       :person/password "Abc123!@#",
       :person/role :customer,
       :person/agreed? true,
       :xt/id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e"})
