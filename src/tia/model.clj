(ns tia.model
  (:require
   [malli.core :as m]
   [tia.data :as d]
   [tia.util :as u]))

(def tick
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:tick/id :uuid]
   [:tick/timestamp inst?]])

(comment
  (m/validate
   tick
   {:tick/id (u/uuid)
    :tick/timestamp (u/now)}) :=> true)

(def address
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:address/id :uuid]
   [:address/street [:string {:min 1 :max 30}]]
   [:address/unit {:optional true} [:string {:min 1 :max 12}]]
   [:address/city [:string {:min 1 :max 30}]]
   [:address/region (vec (cons :enum (keys d/regions)))]
   [:address/state (vec (cons :enum (keys d/states)))]
   [:address/zip [:string {:min 3 :max 10}]]
   [:address/country (vec (cons :enum (keys d/countries)))]])

(def club
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:club/id :uuid]
   [:club/label [:string {:min 1 :max 30}]]
   [:club/handle :keyword]
   [:club/nudity [:enum :full :topless :bikini]]
   [:club/status [:enum :open :closed :temp-closed]]
   [:club/website {:optional true} [:string {:min 1 :max 80}]]
   [:club/facebook {:optional true} [:string {:min 1 :max 60}]]
   [:club/twitterx {:optional true} [:string {:min 1 :max 60}]]
   [:club/instagram {:optional true} [:string {:min 1 :max 60}]]
   [:club/phone [:string {:min 1 :max 20}]]
   [:club/address.id :uuid]])

(comment
  (m/validate
   club
   {:xt/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
    :club/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
    :club/label "Johnny A’s Hitching Post"
    :club/handle :johnny-as-hitching-post
    :club/status :open
    :club/nudity :bikini
    :club/website "http://johnnyashitchingpost.com/"
    :club/facebook "https://www.facebook.com/JohnnyAsHitchingPost"
    :club/twitterx "https://twitter.com/hitchingpostnj"
    :club/instagram "https://www.instagram.com/Hitching_Post_/"
    :club/phone "1-973-684-7678"
    :club/address.id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"})
  (m/validate
   address
   {:address/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
    :address/street "95 Barclay St"
    :address/city "Paterson"
    :address/state :nj
    :address/zip "07503"
    :address/country :usa}))

(def division
  (vec (cons :enum (keys d/divisions))))

(def state
  (vec (cons :enum (keys d/states))))

(def nickname
  [:and
   [:string {:min 4 :max 15}]
   [:re #"^[a-zA-Z0-9]+$"]])

(def email
  [:and
   [:string {:min 5 :max 15}]
   [:re #"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$"]])

(def password
  [:re #"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$"])

(def person
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:person/id :uuid]
   [:person/nickname nickname]
   [:person/email email]
   [:person/password password]
   [:person/role [:enum :customer :dancer :staff]]
   [:person/preferences :map]])

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
