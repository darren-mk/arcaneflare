(ns arcaneflare.common.schema
  (:require [clojure.spec.alpha :as s]))

(s/def :xt/id
  uuid?)

(s/def :email/address
  (let [rgx #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$"]
    (s/and string?
           #(re-matches rgx %))))

(s/def :email/verified?
  boolean?)

(s/def :email/object
  (s/keys :req [:xt/id
                :email/address
                :email/verified?]))

(s/def :person/username
  string?)

(s/def :person/email-id
  uuid?)

(s/def :person/role
  #{:customer :provider :owner :staff})

(s/def :person/object
  (s/keys :req [:xt/id
                :person/username
                :person/email-id
                :person/role]))
