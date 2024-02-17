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

(def address
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:address/id :uuid]
   [:address/street [:string {:min 2 :max 30}]]
   [:address/city [:string {:min 2 :max 30}]]
   [:address/state [:string {:min 2 :max 15}]]
   [:address/zip [:string {:min 3 :max 10}]]
   [:address/country [:string {:min 2 :max 20}]]])

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

(def image
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:image/id :uuid]
   [:image/person-id {:optional true} :uuid]
   [:image/profile-id {:optional true} :uuid]
   [:image/place-id {:optional true} :uuid]
   [:image/objk [:string {:min 5 :max 40}]]
   [:image/filename [:string {:min 1 :max 60}]]
   [:image/size :int]])

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
   [:post/title {:optional true} [:string {:min 1 :max 60}]]
   [:post/kind [:enum :review :article :event]]
   [:post/detail :string]
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
   [:commentary/content :string]
   [:commentary/created inst?]
   [:commentary/updated inst?]
   [:commentary/post-id {:optional true} :uuid]
   [:commentary/person-id :uuid]])
