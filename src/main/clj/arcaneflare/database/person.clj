(ns arcaneflare.database.person
  (:require
   [arcaneflare.common.schema]
   [arcaneflare.database.api :as a]
   [clojure.spec.alpha :as s]))

(s/fdef new-username-avail?
  :args (s/cat :s :person/username)
  :ret boolean?)

(defn new-username-avail? [db s]
  (zero? (a/count-all-having-kv
         db :person/username s)))

(defn create! [node person]
  (s/assert :person/object person)
  (a/put! node person))

(defn count-people [db]
  (a/count-all-having-key
   db :person/username))

(s/fdef find-by-email
  :args (s/cat :email :person/email)
  :ret :person/object)

(defn find-by-email-address
  [db email-address]
  (ffirst
   (a/query db
    '{:find [(pull ?person [*])]
      :in [[?email-address]]
      :where [[?person :person/email-id ?email-id]
              [?email :xt/id ?email-id]
              [?email :email/address ?email-address]]}
    [email-address])))
