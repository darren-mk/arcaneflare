(ns tia.db.post
  (:require
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.db.recency :as r]
   [tia.model :as model]))

(defn create! [{:post/keys [id] :as post}]
  (assert (m/validate model/post post))
  (r/upsert-recency! id)
  (dbc/put! (merge {:xt/id id} post)))

#_
(defn get-posts-by-place [pid]
  (dbc/query
   '{:find [(pull ?post [*])]
     :in [[?pid]]
     :where [[?post :post/place-id ?pid]]}
   [pid]))

(defn get-by-handle [handle kind]
  (let [qr '{:find [(pull ?post [*]) ?timestamp]
             :in [[?handle ?kind]]
             :where [[?place :place/handle ?handle]
                     [?place :place/id ?place-id]
                     [?post :post/place-id ?place-id]
                     [?post :post/kind ?kind]
                     [?post :post/id ?post-id]
                     [?recency :recency/post-id ?post-id]
                     [?recency :recency/timestamp ?timestamp]]
             :order-by [[?timestamp :desc]]}]
    (map first (dbc/query qr [handle kind]))))

(comment
  (get-by-handle :silk-gentlemens-club :review)
  :=> '({:post/updated #inst "2024-02-17T03:43:44.464-00:00",
         :post/person-id #uuid "11381509-5e3b-448b-958d-6a23b242ce61",
         :post/created #inst "2024-02-17T03:43:44.464-00:00",
         :post/title "koko",
         :post/id #uuid "443edcf8-6d16-4ee0-9246-395cf989aac6",
         :post/kind :review,
         :post/place-id #uuid "0f16d843-8d5d-4670-bdad-dc2fde38cee8",
         :xt/id #uuid "443edcf8-6d16-4ee0-9246-395cf989aac6",
         :post/detail "lolo"}
        {:post/updated #inst "2024-02-17T03:47:35.748-00:00",
         :post/person-id #uuid "11381509-5e3b-448b-958d-6a23b242ce61",
         :post/created #inst "2024-02-17T03:47:35.748-00:00",
         :post/title "popo",
         :post/id #uuid "abe5eea4-3eed-4245-9f8c-fccda81d444c",
         :post/kind :review,
         :post/place-id #uuid "0f16d843-8d5d-4670-bdad-dc2fde38cee8",
         :xt/id #uuid "abe5eea4-3eed-4245-9f8c-fccda81d444c",
         :post/detail "mama"})
  (dbc/count-all-having-key :post/id)
  :=> 2)
