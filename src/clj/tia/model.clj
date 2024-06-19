(ns tia.model
  (:require
   [tia.data :as d]))

(def tick
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:tick/id :uuid]
   [:tick/timestamp inst?]])

(def language
  "following iso 639 language codes"
  [:enum :en :es :fr])

#_
(def migration
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:migration/id :uuid]
   [:migration/number :int]
   [:migration/label [:string {:min 2 :max 30}]]
   [:migration/at inst? ]])

#_
(def nation
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:nation/id :uuid]
   [:nation/label [:string {:min 2 :max 30}]]
   [:nation/acronym [:string {:max 5}]]])

#_
(def state
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:state/id :uuid]
   [:state/label [:string {:min 2 :max 30}]]
   [:state/acronym [:string {:max 5}]]
   [:state/nation-id :uuid]])

#_
(def county
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:county/id :uuid]
   [:county/label [:string {:min 2 :max 30}]]
   [:county/state-id :uuid]])

#_
(def city
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:city/id :uuid]
   [:city/label [:string {:min 2 :max 30}]]
   [:city/county-id :uuid]])

(def address
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:address/id :uuid]
   [:address/number [:string {:min 2 :max 30}]]
   [:address/street [:string {:min 2 :max 30}]]
   [:address/city-id :uuid]
   [:address/zip [:string {:min 3 :max 10}]]])

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

 (def division
  (vec (cons :enum (keys d/divisions))))

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
