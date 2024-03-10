(ns tia.model
  (:require
   [tia.data :as d]))

(def ticking
  [:map {:closed true}
   [:ticking_id :uuid]
   [:created_at inst?]])

(def language
  "following iso 639 language codes"
  [:enum :en :es :fr])

(def address
  [:map {:closed true}
   [:address/id :uuid]
   [:address/street [:string {:min 2 :max 80}]]
   [:address/city [:string {:min 2 :max 30}]]
   [:address/state [:string {:min 2 :max 30}]]
   [:address/zip [:string {:min 3 :max 15}]]
   [:address/country [:string {:min 2 :max 30}]]
   [:address/created-at {:optional true} inst?]
   [:address/updated-at {:optional true} inst?]])

(def industries
  [:enum :strip-club :massage-parlor])

(def nudities
  [:enum :full :top :none :unknown])

(def statuses
  [:enum :operational :closed :temp-closed])

(def place
  [:map {:closed true}
   [:place/id :uuid]
   [:place/industry industries]
   [:place/label [:string {:min 1 :max 60}]]
   [:place/handle :keyword]
   [:place/nudity nudities]
   [:place/status statuses]
   [:place/address-id :uuid]
   [:place/website {:optional true} [:string {:min 1 :max 120}]]
   [:place/facebook {:optional true} [:string {:min 1 :max 100}]]
   [:place/twitterx {:optional true} [:string {:min 1 :max 100}]]
   [:place/instagram {:optional true} [:string {:min 1 :max 100}]]
   [:place/phone {:optional true} [:string {:min 1 :max 30}]]
   [:place/google-id {:optional true} [:string {:min 1 :max 60}]]
   [:place/google-uri {:optional true} [:string {:min 1 :max 60}]]])

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

(def jobs
  [:enum :customer :provider :owner :staff])

(def person
  [:map {:closed true}
   [:person/id :uuid]
   [:person/nickname nickname]
   [:person/email email]
   [:person/password password]
   [:person/job jobs]
   [:person/verified :boolean]])

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
   [:session/expiration inst?]])

(def literatures
  [:enum :review :article :event])

(def curbs [:enum :removed :banned :none])

(def post
  [:map {:closed true}
   [:post/id :uuid]
   [:post/title [:string {:min 2 :max 60}]]
   [:post/literature literatures]
   [:post/curb curbs]
   [:post/detail [:string {:min 3}]]
   [:post/place-id {:optional true} :uuid]
   [:post/region {:optional true} :map]
   [:post/created-at inst?]
   [:post/edited-at inst?]
   [:post/author-id :uuid]])

(def commentary
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:commentary/id :uuid]
   [:commentary/content [:string {:min 3}]]
   [:commentary/created inst?]
   [:commentary/updated inst?]
   [:commentary/post-id :uuid]
   [:commentary/person-id :uuid]])

(def setting
  [:map {:closed true}
   [:xt/id {:optional true} :keyword]
   [:setting/id :keyword]
   [:setting/session-expiration-days :int]
   [:setting/admin-password [:string {:min 5 :max 15}]]])
