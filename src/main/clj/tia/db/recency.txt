(ns tia.db.recency
   (:require
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn find-recency-id [post-id]
  (ffirst
   (dbc/query
    '{:find [?recency-id]
      :in [[?post-id]]
      :where [[?recency :recency/post-id ?post-id]
              [?recency :recency/id ?recency-id]]}
    [post-id])))

(comment
  (find-recency-id #uuid "2c03a573-9eda-4800-8481-ae5c3f664c00")
  :=> #uuid "c1f2a7d4-3b66-45d4-8ccc-e47066e377ff")

(defn upsert-recency!
  ([post-id commentary-id]
   (let [id (or (find-recency-id post-id) (u/uuid))
         recency (merge {:recency/id id
                         :recency/post-id post-id
                         :recency/timestamp (u/now)}
                        (when commentary-id
                          {:recency/commentary-id commentary-id}))
         m (merge {:xt/id id} recency)]
     (assert (m/validate model/recency recency))
     (dbc/put! m)))
  ([post-id]
   (upsert-recency! post-id nil)))

(comment
  (upsert-recency! #uuid "2c03a573-9eda-4800-8481-ae5c3f664c00")
  :=> #:xtdb.api{:tx-id 1971,
                 :tx-time #inst "2024-03-09T17:27:29.913-00:00"})
