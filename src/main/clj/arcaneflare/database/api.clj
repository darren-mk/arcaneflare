(ns arcaneflare.database.api
  "internal api to xtdb"
  (:require
   [xtdb.api :as xt]))

(def ->db
  xt/db)

(def atx
  xt/await-tx)

(def ->node
  xt/start-node)

(defn query
  ([db ql]
   (xt/q db ql))
  ([db ql var]
   (xt/q db ql var)))

(defn ent [db id]
  (xt/entity db id))

(defn put! [node m]
  (xt/submit-tx
   node [[::xt/put m]]))

(defn pull-by-id [db id]
  (let [ql '{:find [(pull ?e [*])]
             :in [[id]]
             :where [[?e :xt/id id]]}]
    (ffirst (query db ql [id]))))

(defn created-at [db id]
  (-> (xt/entity-history db id :asc)
      first (get :xtdb.api/tx-time)))

(defn last-edited-at [db id]
  (-> (xt/entity-history db id :desc)
      first (get :xtdb.api/tx-time)))

(defn pull-all-having-key [db k]
  (let [ql {:find '[(pull ?e [*])]
            :where [['?e k]]}]
    (map first (query db ql))))

(defn pull-all-having-kv [db k v]
  (let [ql {:find '[(pull ?e [*])]
            :in [['v]]
            :where [['?e k 'v]]}]
    (map first (query db ql [v]))))

(defn count-all-having-key [db k]
  (let [ql {:find '[(count ?e)]
            :where [['?e k]]}]
    (or (ffirst (query db ql)) 0)))

(defn count-all-having-kv [db k v]
  (let [ql {:find '[(count ?e)]
            :in [['?v]]
            :where [['?e k '?v]]}]
    (or (ffirst (query db ql [v])) 0)))

(defn update!
  "record data only when the existing
  data is not identical to the new one"
  [node m]
  (let [id (get m :xt/id)
        existing (pull-by-id (xt/db node) id)]
    (when (not= existing m)
      (put! node m))))

(defn delete! [node id]
  (xt/submit-tx
   node 
   [[::xt/delete id]]))
