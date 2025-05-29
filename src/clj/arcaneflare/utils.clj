(ns arcaneflare.utils
  "utility functions that involve side effects
  or that are not regarding domain logic."
  (:require
   [clojure.spec.alpha :as s]
   [tick.core :as t]))

(s/fdef uuid
  :args (s/cat)
  :ret uuid?)

(defn uuid []
  (java.util.UUID/randomUUID))

(s/fdef ->sql-dt
  :args (s/cat :n inst?)
  :ret inst?)

(defn sql-dt [dt]
  (-> dt .getTime
      java.sql.Timestamp.))

(defn now []
  (sql-dt (java.util.Date.)))

(s/fdef after-days
  :args (s/cat :n int?)
  :ret inst?)

(defn after-days [n]
  (-> (t/instant)
      (t/>> (t/new-duration n :days))
      (t/inst) sql-dt))

(s/fdef after-minutes
  :args (s/cat :n int?)
  :ret inst?)

(defn after-minutes [n]
  (t/inst
   (t/>> (t/instant)
         (t/new-duration n :minutes))))

(s/fdef past?
  :args (s/cat :n inst?)
  :ret boolean?)

(defn past? [d]
  (t/< (t/instant d)
       (t/instant)))

(s/fdef map->nsmap
  :args (s/cat :m map? :ns keyword?)
  :ret map?)

(defn map->nsmap
  "Apply the string n to the supplied structure m as a namespace."
  [m ns]
  (let [->nsk (fn [a b] (keyword (name a) (name b)))
        f (fn [acc [k v]] (assoc acc (->nsk ns k) v))]
    (reduce f {} m)))

(s/fdef update-if-exists
  :args (s/cat :m map? :k keyword? :f fn?)
  :ret map?)

(defn update-existing [m k f]
  (if (get m k)
    (update m k f)
    m))

(defn fail-nil [any]
  (or any (throw (ex-info "nil!" {}))))
