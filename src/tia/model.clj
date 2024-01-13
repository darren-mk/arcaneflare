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

(def migration
  [:map {:closed true}
   [:xt/id {:optional true} :keyword]
   [:migration/id :keyword]
   [:migration/file :string]
   [:migration/time inst?]])

(def address
  [:map {:closed true}
   [:xt/id {:optional true} :uuid]
   [:address/id :uuid]
   [:address/street [:string {:min 1 :max 30}]]
   [:address/unit {:optional true} [:string {:min 1 :max 12}]]
   [:address/city [:string {:min 1 :max 30}]]
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
