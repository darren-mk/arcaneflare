(ns tia.util
  "utility functions that involve side effects
  or that are not regarding domain logic."
  (:require
   [malli.core :as m]
   [tick.core :as t]))

(defn uuid []
  (java.util.UUID/randomUUID))

(comment
  (uuid)
  :=> #uuid "7baa6a7a-cc90-4219-a80a-cac6c33c9101")

(defn now []
  (-> (java.util.Date.)
      .getTime
      java.sql.Timestamp.))

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

(defn retry-check-existence [{:keys [interval max f]}]
  (let [result (f)]
    (if (and (nil? result) (< 1 max))
      (do (Thread/sleep interval)
          (retry-check-existence {:interval interval
                  :max (dec max)
                  :f f}))
      result)))

(defn retry-check-deletion
  [{:keys [interval max f]}]
  (let [read (f)]
    (when (and (some? read) (< 1 max))
      (Thread/sleep interval)
      (retry-check-existence
       {:interval interval
        :max (dec max)
        :f f}))))

(m/=> map->nsmap
      [:=> [:cat :map :keyword]
       :map])

(defn map->nsmap
  "Apply the string n to the supplied structure m as a namespace."
  [m ns]
  (let [->nsk (fn [a b] (keyword (name a) (name b)))
        f (fn [acc [k v]] (assoc acc (->nsk ns k) v))]
    (reduce f {} m)))

(comment
  (map->nsmap {:a 1 :b 2} :hello)
  :=> #:hello{:a 1, :b 2})

(defn update-if-exists [m k f]
  (if (get m k)
    (update m k f)
    m))

(comment
  (update-if-exists {:a 1} :a inc) :=> {:a 2}
  (update-if-exists {:a 1} :b inc) :=> {:a 1})

(defmacro mf [fname sigs]
  `(malli.core/=>
    ~fname
    [:=> (vec (concat [:cat] (butlast ~sigs)))
     (last ~sigs)]))

(comment
  (defn callme [x] x)
  (mf callme [:string :string])
  :=> tia.util/callme)
