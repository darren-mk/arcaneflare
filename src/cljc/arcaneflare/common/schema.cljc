(ns arcaneflare.common.schema
  #_(:require [clojure.spec.alpha :as s]))
#_
(s/def :person/id uuid?)

#_
(s/def :person/email
  (let [rgx #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$"]
    (s/and string? #(re-matches rgx %))))
#_
(s/def :person/password string?)
#_
(s/def :person/role
  #{:role/customer
    :role/provider
    :role/owner
    :role/staff})
#_
(s/def :state/label
  string?)
#_
(s/def :state/acronym
  string?)
#_
(s/def :state/country-id
  uuid?)
#_
(s/def :state/object
  (s/keys :req [:xt/id
                :state/label
                :state/acronym
                :state/country-id]))
#_
(s/def :district/label
  string?)
#_
(s/def :district/state-id
  uuid?)
#_
(s/def :district/object
  (s/keys :req [:xt/id
                :district/label
                :district/state-id]))
#_
(s/def :city/label
  string?)
#_
(s/def :city/district-id
  uuid?)
#_
(s/def :city/object
  (s/keys :req [:xt/id
                :city/label
                :city/district-id]))
#_
(s/def :address/street
  string?)
#_
(s/def :address/zip
  (s/and string?
         #(re-matches
           #"^[0-9]{5}(?:-[0-9]{4})?$" %)))
#_
(s/def :address/city-id
  uuid?)
#_
(def language
  "following iso 639 language codes"
  [:enum :en :es :fr])
#_
(def industry
  [:enum :strip-club :parlor])
#_
(def place
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:place/id :uuid]
   [:place/language language]
   [:place/industry industry]
   [:place/label [:string {:min 1 :max 60}]]
   [:place/handle :keyword]
   [:place/nudity {:optional true} [:enum :full :top :none :unknown]]
   [:place/status [:enum :operational :closed :temp-closed]]
   [:place/website {:optional true} [:string {:min 1 :max 120}]]
   [:place/facebook {:optional true} [:string {:min 1 :max 100}]]
   [:place/twitterx {:optional true} [:string {:min 1 :max 100}]]
   [:place/instagram {:optional true} [:string {:min 1 :max 100}]]
   [:place/phone {:optional true} [:string {:min 1 :max 30}]]
   [:place/google-id {:optional true} [:string {:min 1 :max 60}]]
   [:place/google-uri {:optional true} [:string {:min 1 :max 60}]]
   [:place/address-id :uuid]])
#_
(def nickname
  [:and
   [:string {:min 4 :max 15}]
   [:re #"^[a-zA-Z0-9]+$"]])
#_
(def email
  "consider adding regex validation"
  [:and
   [:string {:min 5 :max 40}]])
#_
(def password
  [:re #"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?!.* ).{8,16}$"])
#_
(def roles
  [:enum :customer :performer
   :provider :owner :staff])
#_
(def person
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:person/id :uuid]
   [:person/nickname nickname]
   [:person/email email]
   [:person/password password]
   [:person/role roles]
   [:person/agreed? :boolean]
   [:person/preferences {:optional true} :map]])
#_
(def file
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:file/id :uuid]
   [:file/post-id :uuid]
   [:file/kind [:enum :image :video :document]]
   [:file/objk [:string {:min 5 :max 60}]]
   [:file/designation [:string {:min 1 :max 60}]]
   [:file/size :int]])
#_
(def profile
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:profile/id :uuid]
   [:profile/person-id :uuid]
   [:profile/phrase :string]
   [:profile/place-ids [:set :uuid]]])
#_
(def session
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:session/id :uuid]
   [:session/person-id :uuid]
   [:session/renewal inst?]
   [:session/expiration inst?]])
#_
(def post
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:post/id :uuid]
   [:post/title {:optional true} [:string {:min 2 :max 60}]]
   [:post/kind [:enum :review :article :event]]
   [:post/cover {:optional true} [:enum :removed :banned]]
   [:post/detail [:string {:min 3}]]
   [:post/place-id {:optional true} :uuid]
   [:post/region {:optional true} :map]
   [:post/image-ids {:optional true} [:set :uuid]]
   [:post/created inst?]
   [:post/updated inst?]
   [:post/person-id :uuid]])

#_
(def setting
  [:map {:closed true}
   [:xt/id {:optional true} :keyword]
   [:setting/id :keyword]
   [:setting/session-expiration-days :int]
   [:setting/admin-password [:string {:min 5 :max 15}]]])
