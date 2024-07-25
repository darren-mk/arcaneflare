(ns arcaneflare.database.common
  (:require
   [arcaneflare.database.core :as dbc]
   [xtdb.api :as xt]))

(defn query
  ([ql]
   (xt/q (xt/db dbc/node) ql))
  ([ql var]
   (xt/q (xt/db dbc/node) ql var)))

(defn put! [m]
  (xt/submit-tx
   dbc/node [[::xt/put m]]))

(defn pull-by-id [id]
  (let [ql '{:find [(pull ?e [*])]
             :in [[id]]
             :where [[?e :xt/id id]]}]
    (ffirst (query ql [id]))))

(defn created-at [id]
  (-> (xt/db dbc/node)
      (xt/entity-history id :asc)
      first
      (get :xtdb.api/tx-time)))

(defn last-edited-at [id]
  (-> (xt/db dbc/node)
      (xt/entity-history id :desc)
      first
      (get :xtdb.api/tx-time)))

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

(defn update!
  "record data only when the existing
  data is not identical to the new one"
  [m]
  (let [id (get m :xt/id)
        existing (pull-by-id id)]
    (when (not= existing m)
      (put! m))))

(defn delete! [id]
  (xt/submit-tx
   dbc/node
   [[::xt/delete id]]))
