(ns tia.db.common
  (:require
   [clojure.tools.logging :as log]
   [tia.db.core :as dbcr :refer [db]]
   [tia.util :as u]
   [malli.core :as m]
   [xtdb.api :as xt]))

(defn query
  ([ql]
   (xt/q (xt/db db) ql))
  ([ql var]
   (xt/q (xt/db db) ql var)))

(defn record! [data]
  (let [ns-s (u/nsmap->ns data)
        idk (u/ns->idk ns-s)
        schema (u/ns->schema ns-s)
        idv (get data idk)
        m (assoc data :xt/id idv)]
    (if (m/validate schema m)
      (xt/submit-tx db [[::xt/put m]])
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
    (query ql)))

(defn count-all-having-key [k]
  (let [ql {:find '[(count ?e)]
            :where [['?e k]]}]
    (or (ffirst (query ql)) 0)))

(defn count-all []
  (count-all-having-key :xt/id))

(defn merge! [data]
  (let [ns-s (u/nsmap->ns data)
        idk (u/ns->idk ns-s)
        idv (get data idk)
        ex-m (pull-by-id idv)
        m (merge ex-m data)]
    (record! m)))

(defn delete! [id]
  (xt/submit-tx db [[::xt/delete id]]))

(comment
  (count-all))
