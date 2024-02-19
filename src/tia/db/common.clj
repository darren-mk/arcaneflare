(ns tia.db.common
  (:require
   [clojure.tools.logging :as log]
   [tia.db.core :as dbcr :refer [db]]
   [tia.calc :as c]
   [malli.core :as m]
   [xtdb.api :as xt]))

(defn query
  ([ql]
   (xt/q (xt/db db) ql))
  ([ql var]
   (xt/q (xt/db db) ql var)))

(defn- put! [m]
  (xt/submit-tx
   db [[::xt/put m]]))

(defn record! [data]
  (let [ns-s (c/nsmap->ns data)
        idk (c/ns->idk ns-s)
        schema (c/ns->schema ns-s)
        idv (get data idk)
        m (assoc data :xt/id idv)]
    (if (m/validate schema m)
      (put! m)
      (log/error "data not validated:"
                 (m/explain schema m)))))

(defn pull-by-id [id]
  (let [ql '{:find [(pull ?e [*])]
             :in [[id]]
             :where [[?e :xt/id id]]}]
    (ffirst (query ql [id]))))

(defn pull-all-having-key [k]
  (let [ql {:find '[(pull ?e [*])]
            :where [['?e k]]}]
    (map first (query ql))))

(defn pull-all-having-kv [k v]
  (let [ql {:find '[(pull ?e [*])]
            :in [['v]]
            :where [['?e k 'v]]}]
    (map first (query ql [v]))))

(defn count-all-having-key [k]
  (let [ql {:find '[(count ?e)]
            :where [['?e k]]}]
    (or (ffirst (query ql)) 0)))

(defn count-all-having-kv [k v]
  (let [ql {:find '[(count ?e)]
            :in [['?v]]
            :where [['?e k '?v]]}]
    (or (ffirst (query ql [v])) 0)))

(defn count-all []
  (count-all-having-key :xt/id))

(defn upsert!
  "record data only when the existing
  data is not identical to the new one"
  [data]
  (let [ns (c/nsmap->ns data)
        idk (c/ns->idk ns)
        idv (get data idk)
        ex (pull-by-id idv)]
    (when (not= ex data)
      (record! data))))

(comment
  (upsert!
   (let [id #uuid "d9fb6bf4-4009-421a-a1fd-046d05b72772"
         ts #inst "2024-02-03T03:39:45.580-00:00"]
     {:xt/id id
      :tick/id id
      :tick/timestamp ts})))

(defn delete! [id]
  (xt/submit-tx db [[::xt/delete id]]))

(comment
  (count-all)
  (delete! :id)
  (pull-all-having-key :xt/id)
  (count-all-having-kv
   :address/street "50 West 33rd Street")
  :=> 1)
