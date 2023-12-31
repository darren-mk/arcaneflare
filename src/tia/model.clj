(ns tia.model
  (:require
   [malli.core :as m]
   [tia.data :as d]
   [tia.util :as u]))

(def tick
  [:map
   [:tick/id :uuid]
   [:tick/timestamp inst?]])

(comment
  (m/validate
   tick
   {:tick/id (u/uuid)
    :tick/timestamp (u/now)}) :=> true)

(def address
  [:map
   [:address/id :uuid]
   [:address/street [:string {:min 1 :max 30}]]
   [:address/unit {:optional true} [:string {:min 1 :max 12}]]
   [:address/city [:string {:min 1 :max 30}]]
   [:address/state (vec (cons :enum (keys d/states)))]
   [:address/zip [:string {:min 3 :max 10}]]
   [:address/country (vec (cons :enum (keys d/countries)))]])

(comment
  (m/validate
   address
   {:address/id #uuid "dc936b81-a896-45cf-8f7b-17eae0044565"
    :address/street "1 Tona Street"
    :address/unit "#3"
    :address/city "Paramus"
    :address/state :nj
    :address/zip "06789"
    :address/country :usa}) :=> true)

(def club
  [:map
   [:club/id :uuid]
   [:club/full-name [:string {:min 1 :max 30}]]
   [:club/status [:enum :open :closed :temp-closed]]
   [:club/address.id :uuid]])

(comment
  (m/validate
   club
   {:xt/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
    :club/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
    :club/full-name "A Latin Amigos"
    :club/status :open
    :club/address.id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"}))
