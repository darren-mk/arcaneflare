(ns arcaneflare.database.person
  (:require
   [arcaneflare.common.schema]
   [arcaneflare.database.interface :as i]
   [clojure.spec.alpha :as s]))

(defn new-username-avail? [db s]
  (zero? (i/count-by-kv
         db :person/username s)))

(defn create! [node person]
  (s/assert :person/object person)
  (i/create-single! node person))

(defn count-people [db]
  (i/count-by-k
   db :person/username))

(s/fdef find-by-email
  :args (s/cat :email :person/email)
  :ret :person/object)

(defn find-by-email-address
  [db email-address]
  (ffirst
   (i/query db
    '{:find [(pull ?person [*])]
      :in [[?email-address]]
      :where [[?person :person/email-id ?email-id]
              [?email :xt/id ?email-id]
              [?email :email/address ?email-address]]}
    [email-address])))
