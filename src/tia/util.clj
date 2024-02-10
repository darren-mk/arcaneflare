(ns tia.util
  "utility functions that involve side effects
  or that are not regarding domain logic."
  (:require
   [malli.core :as m]
   [tick.core :as t]))

(defn uuid []
  (java.util.UUID/randomUUID))

(defn now []
  (java.util.Date.))

(m/=> after-days
      [:=> [:cat :int] inst?])

(defn after-days [n]
  (t/inst
   (t/>> (t/instant)
         (t/new-duration n :days))))

(comment
  (after-days 30)
  :=> #inst "2024-02-21T15:43:47.621-00:00")

(defn after-minutes [n]
  (t/inst
   (t/>> (t/instant)
         (t/new-duration n :minutes))))

(comment
  (after-minutes 3)
  :=> #inst "2024-02-10T13:07:31.771-00:00")

(defn past? [d]
  (t/< (t/instant d)
       (t/instant)))

(comment
  (past? #inst "2001-02-21T15:43:47.621-00:00")
  :=> true
  (past? #inst "2099-02-21T15:43:47.621-00:00")
  :=> false)

(defn obj->str [obj]
 (.toString obj))
