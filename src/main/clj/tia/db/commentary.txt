(ns tia.db.commentary
  (:require
   [tia.db.common :as common]))

(defn get-all-of-post [post-id]
  (let [qr '{:find [(pull ?commentary [*])]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/updated ?updated]]}
        raw (common/query qr [post-id])]
    (->> raw
         (map first)
         (sort-by :commentary/updated))))

(defn get-latest-of-post [post-id]
  (let [qr '{:find [?id ?updated ?person-id ?nickname]
             :keys [latest-commentary-id latest-commentary-updated
                    latest-commentary-commenter-id
                    latest-commentary-commenter-nickname]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/id ?id]
                     [?commentary :commentary/person-id ?person-id]
                     [?commentary :commentary/updated ?updated]
                     [?person :person/id ?person-id]
                     [?person :person/nickname ?nickname]]
             :order-by [[?updated :desc]]
             :limit 1}]
    (first (common/query qr [post-id]))))

(comment
  (get-latest-of-post
   #uuid"2c03a573-9eda-4800-8481-ae5c3f664c00")
   :=> {:latest-commentary-id
        #uuid "472c8c5a-dc4f-43a9-a6c5-511aafa20cda",
       :latest-commentary-updated
        #inst "2024-03-04T13:23:46.583-00:00",
       :latest-commentary-commenter-id
        #uuid "364f9537-8c24-4520-8586-815c33dddbbd",
       :latest-commentary-commenter-nickname "hello"})

(defn count-by-post [post-id]
  (let [qr '{:find [(count ?commentary-id)]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/id ?commentary-id]]}]
    (or (ffirst (common/query qr [post-id])) 0)))

(comment
  (count-by-post
   #uuid"2c03a573-9eda-4800-8481-ae5c3f664c00")
  :=> 2)
(ns tia.db.commentary
  (:require
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.db.recency :as r]
   [tia.model :as model]))

(defn create!
  [{:commentary/keys [id post-id] :as commentary}]
  (assert (m/validate model/commentary commentary))
  (r/upsert-recency! post-id id)
  (dbc/put! (merge {:xt/id id} commentary)))
