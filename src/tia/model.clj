(ns tia.model
  (:require
   [tia.data :as d]))

(def ticking
  [:map {:closed true}
   [:ticking/id :uuid]
   [:ticking/created-at inst?]])

(def address
  [:map {:closed true}
   [:address/id :uuid]
   [:address/street [:string {:min 2 :max 80}]]
   [:address/city [:string {:min 2 :max 30}]]
   [:address/state [:string {:min 2 :max 30}]]
   [:address/zip [:string {:min 3 :max 15}]]
   [:address/country [:string {:min 2 :max 30}]]
   [:address/created-at {:optional true} inst?]
   [:address/edited-at {:optional true} inst?]])

(def sector
  [:enum :strip-club :massage-parlor])

(def nudity
  [:enum :full :top :none
   :unknown :unimportant])

(def status
  [:enum :operational :closed :temp-closed])

(def misc
  [:map
   [:website {:optional true} [:string {:min 1 :max 120}]]
   [:facebook {:optional true} [:string {:min 1 :max 100}]]
   [:twitterx {:optional true} [:string {:min 1 :max 100}]]
   [:instagram {:optional true} [:string {:min 1 :max 100}]]
   [:phone {:optional true} [:string {:min 1 :max 30}]]
   [:google-id {:optional true} [:string {:min 1 :max 60}]]
   [:google-uri {:optional true} [:string {:min 1 :max 60}]]])

(def place
  [:map {:closed true}
   [:place/id :uuid]
   [:place/sector sector]
   [:place/label [:string {:min 1 :max 60}]]
   [:place/handle :keyword]
   [:place/nudity nudity]
   [:place/status status]
   [:place/address-id :uuid]
   [:place/misc misc]
   [:place/created-at inst?]
   [:place/edited-at inst?]])

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

(def job
  [:enum :customer :provider :owner :staff])

(def person
  [:map {:closed true}
   [:person/id :uuid]
   [:person/nickname nickname]
   [:person/email email]
   [:person/password password]
   [:person/job job]
   [:person/verified? :boolean]
   [:person/created-at inst?]
   [:person/edited-at inst?]])

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
   [:session/id :uuid]
   [:session/person-id :uuid]
   [:session/created-at inst?]
   [:session/expired-at inst?]])

(def subject
  [:enum :review :article :event])

(def curb [:enum :removed :banned :none])

(def location
  [:map
   [:place-id {:optional true} :uuid]
   [:city {:optional true} :string]
   [:state {:optional true} :string]
   [:country {:optional true} :string]])

(def post
  [:map {:closed true}
   [:post/id :uuid]
   [:post/subject subject]
   [:post/curb curb]
   [:post/author-id :uuid]
   [:post/location location]
   [:post/title [:string {:min 2 :max 60}]]
   [:post/detail [:string {:min 3}]]
   [:post/created-at inst?]
   [:post/edited-at inst?]])

(def commentary
  [:map {:closed true}
   [:commentary/id :uuid]
   [:commentary/post-id :uuid]
   [:commentary/annotator-id :uuid]
   [:commentary/content [:string {:min 3}]]
   [:commentary/created-at inst?]
   [:commentary/edited-at inst?]])

(def setting
  [:map {:closed true}
   [:xt/id {:optional true} :keyword]
   [:setting/id :keyword]
   [:setting/session-expiration-days :int]
   [:setting/admin-password [:string {:min 5 :max 15}]]])
