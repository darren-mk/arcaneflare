(ns arcaneflare.database.email
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(s/fdef new-email-avail?
  :args (s/cat :s :email/address)
  :ret boolean?)

(defn new-email-address-avail? [db s]
  (zero? (i/count-by-kv
          db :email/address s)))

(defn create! [node email]
  (s/assert :email/object email)
  (i/create-single! node email))

(defn verify! [node email]
  (s/assert :email/object email)
  (let [old email
        new (assoc email :email/verified? true)]
    (i/update-single! node old new)))
