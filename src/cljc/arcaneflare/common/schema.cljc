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
  #{:role/customer
    :role/provider
    :role/owner
    :role/staff})

(s/def :person/object
  (s/keys :req [:xt/id
                :person/username
                :person/email-id
                :person/role]))

(s/def :script/number
  int?)

(s/def :script/description
  string?)

(s/def script/object
  (s/keys :req [:xt/id
                :script/number
                :script/description]))

(s/def :country/label
  string?)

(s/def :country/acronym
  string?)

(s/def :country/object
  (s/keys :req [:xt/id
                :country/label
                :country/acronym]))

(s/def :state/label
  string?)

(s/def :state/acronym
  string?)

(s/def :state/country-id
  uuid?)

(s/def :state/object
  (s/keys :req [:xt/id
                :state/label
                :state/acronym
                :state/country-id]))

(s/def :district/label
  string?)

(s/def :district/state-id
  uuid?)

(s/def :district/object
  (s/keys :req [:xt/id
                :district/label
                :district/state-id]))

(s/def :city/label
  string?)

(s/def :city/district-id
  uuid?)

(s/def :city/object
  (s/keys :req [:xt/id
                :city/label
                :city/district-id]))

(s/def :address/street
  string?)

(s/def :address/zip
  (s/and string?
         #(re-matches
           #"^[0-9]{5}(?:-[0-9]{4})?$" %)))

(s/def :address/city-id
  uuid?)

(s/def :address/object
  (s/keys :req [:xt/id
                :address/street
                :address/zip
                :address/city-id]))

(def language
  "following iso 639 language codes"
  [:enum :en :es :fr])

(def industry
  [:enum :strip-club :parlor])

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

(def nickname
  [:and
   [:string {:min 4 :max 15}]
   [:re #"^[a-zA-Z0-9]+$"]])

(def email
  "consider adding regex validation"
  [:and
   [:string {:min 5 :max 40}]])

(def password
  [:re #"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?!.* ).{8,16}$"])

(def roles
  [:enum :customer :dancer
   :provider :owner :staff])

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

(def file
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:file/id :uuid]
   [:file/post-id :uuid]
   [:file/kind [:enum :image :video :document]]
   [:file/objk [:string {:min 5 :max 60}]]
   [:file/designation [:string {:min 1 :max 60}]]
   [:file/size :int]])

(def profile
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:profile/id :uuid]
   [:profile/person-id :uuid]
   [:profile/phrase :string]
   [:profile/place-ids [:set :uuid]]])

(def session
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:session/id :uuid]
   [:session/person-id :uuid]
   [:session/renewal inst?]
   [:session/expiration inst?]])

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

(def commentary
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:commentary/id :uuid]
   [:commentary/content [:string {:min 3}]]
   [:commentary/created inst?]
   [:commentary/updated inst?]
   [:commentary/post-id :uuid]
   [:commentary/person-id :uuid]])

(def recency
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:recency/id :uuid]
   [:recency/post-id :uuid]
   [:recency/commentary-id {:optional true} :uuid]
   [:recency/timestamp inst?]])

#_
(def setting
  [:map {:closed true}
   [:xt/id {:optional true} :keyword]
   [:setting/id :keyword]
   [:setting/session-expiration-days :int]
   [:setting/admin-password [:string {:min 5 :max 15}]]])
