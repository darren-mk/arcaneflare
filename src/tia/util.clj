(ns tia.util
  "utility functions that involve side effects
  or that are not regarding domain logic."
  (:require
   [java-time.api :as jt]
   [malli.core :as m]))

(defn uuid []
  (java.util.UUID/randomUUID))

(defn now []
  (java.util.Date.))

(m/=> after-days
      [:=> [:cat :int] inst?])

(defn after-days [n]
  (-> (jt/instant)
      (jt/plus (jt/days n))
      jt/java-date))

(comment
  (after-days 30)
  :=> #inst "2024-02-20T12:12:52.819-00:00")
