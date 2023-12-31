(ns tia.model
  (:require
   [malli.core :as m]
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
