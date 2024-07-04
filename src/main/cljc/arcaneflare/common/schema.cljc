(ns arcaneflare.common.schema
  (:require [clojure.spec.alpha :as s]))

(def email-regex
  #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(s/def :person/id
  uuid?)

(s/def :person/username
  string?)

(s/def :person/email
  (s/and string?
         #(re-matches email-regex %)))

(s/def :person/job
  #{:customer :provider :owner :staff})

(s/def :person/verified
  boolean?)

(s/def :person/object
  (s/keys :req [:person/id
                :person/username
                :person/email
                :person/job
                :person/verified
                :person/created-at
                :person/edited-at]))
