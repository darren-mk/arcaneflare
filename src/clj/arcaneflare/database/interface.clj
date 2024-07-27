(ns arcaneflare.database.interface
  "internal api to xtdb"
  (:require
   [xtdb.api :as xt]))

(def ->db
  xt/db)

(def atx
  xt/await-tx)

(def ->node
  xt/start-node)

(def ->id
  #(get % :xt/id))

(def query
  xt/q)

(def ent
  xt/entity)

(defn create-single-ql [doc]
  [[::xt/match (->id doc) nil]
   [::xt/put doc]])

(defn create-single! [node doc]
  (xt/submit-tx
   node (create-single-ql doc)))

(defn history [db id]
  (xt/entity-history db id :asc))

(defn create-multi-ql [docs]
  (reduce
   (fn [acc v]
     (let [[a b] (create-single-ql v)]
       (-> acc (conj a) (conj b)))) [] docs))

(defn create-multi! [node docs]
  (xt/submit-tx
   node (create-multi-ql docs))
  (xt/sync node))

(defn update-single-ql
  [old new]
  [[::xt/match (->id new) old]
   [::xt/put new]])

(defn update-single! [node old new]
  (xt/submit-tx
   node (update-single-ql old new)))

(defn created-at [db id]
  (-> (history db id)
      first (get :xtdb.api/tx-time)))

(defn last-edited-at [db id]
  (-> (history db id)
      last (get :xtdb.api/tx-time)))

(defn pull-by-k [db k]
  (let [ql {:find '[(pull ?e [*])]
            :where [['?e k]]}]
    (map first (query db ql))))

(defn pull-by-kv [db k v]
  (let [ql {:find '[(pull ?e [*])]
            :in [['v]]
            :where [['?e k 'v]]}]
    (map first (query db ql [v]))))

(defn count-by-k [db k]
  (let [ql {:find '[(count ?e)]
            :where [['?e k]]}]
    (or (ffirst (query db ql)) 0)))

(defn count-by-kv [db k v]
  (let [ql {:find '[(count ?e)]
            :in [['?v]]
            :where [['?e k '?v]]}]
    (or (ffirst (query db ql [v])) 0)))

(defn delete! [node id]
  (xt/submit-tx
   node 
   [[::xt/delete id]]))
