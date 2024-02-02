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

(def coordinate
  [:map
   [:latitude float?]
   [:longitude float?]])

(def country
  (vec (cons :enum (keys d/countries))))

(def state
  (vec (cons :enum (keys d/states))))

(def address
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:address/id :uuid]
   [:address/language language]
   [:address/googleid [:string {:min 1 :max 50}]]
   [:address/googleuri [:string {:min 1 :max 50}]]
   [:address/coordinate coordinate]
   [:address/number [:string {:min 1 :max 12}]]
   [:address/street [:string {:min 1 :max 30}]]
   [:address/city [:string {:min 1 :max 30}]]
   [:address/county [:string {:min 1 :max 30}]]
   [:address/state state]
   [:address/zip [:string {:min 3 :max 10}]]
   [:address/country country]])

(def industry
  [:enum :club :parlor])

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
   [:place/website {:optional true} [:maybe [:string {:min 1 :max 120}]]]
   [:place/facebook {:optional true} [:maybe [:string {:min 1 :max 100}]]]
   [:place/twitterx {:optional true} [:maybe [:string {:min 1 :max 100}]]]
   [:place/instagram {:optional true} [:maybe [:string {:min 1 :max 100}]]]
   [:place/phone [:string {:min 1 :max 25}]]
   [:place/paymethods [:set [:enum :cash :credit :debit]]]
   [:place/schedules vector?]
   [:place/address-id :uuid]])

(def division
  (vec (cons :enum (keys d/divisions))))

(def nickname
  [:and
   [:string {:min 4 :max 15}]
   [:re #"^[a-zA-Z0-9]+$"]])

(def email
  [:and
   [:string {:min 5 :max 15}]
   [:re #"^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$"]])

(def password
  [:re #"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?!.* ).{8,16}$"])

(def person
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:person/id :uuid]
   [:person/nickname nickname]
   [:person/email email]
   [:person/password password]
   [:person/role [:enum :customer :dancer :staff]]
   [:person/agreed? :boolean]
   [:person/preferences {:optional true} :map]])

(def session
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:session/id :uuid]
   [:session/person.id :uuid]
   [:session/renewal inst?]
   [:session/expiration inst?]])

(def post
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:post/id :uuid]
   [:post/title {:optional true} [:string {:min 1 :max 60}]]
   [:post/content :string]
   [:post/created inst?]
   [:post/updated inst?]
   [:post/person.id :uuid]])

(def commentary
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:commentary/id :uuid]
   [:commentary/content :string]
   [:commentary/created inst?]
   [:commentary/updated inst?]
   [:commentary/post.id :uuid]
   [:commentary/person.id :uuid]])
