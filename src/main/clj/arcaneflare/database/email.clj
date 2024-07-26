(ns arcaneflare.database.email
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.api :as a]))

(s/fdef new-email-avail?
  :args (s/cat :s :email/address)
  :ret boolean?)

(defn new-email-address-avail? [db s]
  (zero? (a/count-all-having-kv
          db :email/address s)))

(defn create! [node email]
  (s/assert :email/object email)
  (a/put! node email))
