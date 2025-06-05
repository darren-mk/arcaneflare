(ns arcaneflare.database.post.root
  (:require
   [arcaneflare.database.base :as db.base]))

(defn upsert!
  [{:post/keys [id author-id title body nation
                state county city district place-id]}]
  (let [q {:insert-into :post
           :columns [:id :author-id :title :body
                     :nation :state :county :city
                     :district :place-id :updated-at]
           :values [[id author-id title body
                     nation state county city district
                     place-id [:raw "now()"]]]
           :on-conflict [:id]
           :do-update-set {:author-id :excluded.author_id
                           :title :excluded.title
                           :body :excluded.body
                           :nation :excluded.nation
                           :state :excluded.state
                           :county :excluded.county
                           :city :excluded.city
                           :district :excluded.district
                           :place-id :excluded.place_id
                           :updated-at :excluded.updated_at}}]
    (db.base/run q)))

(defn multi-by
  [{nation :post/nation state :post/state
    county :post/county city :post/city
    district :post/district place-id :post/place-id
    page :post.result/page per :post.result/per}]
  (let [filters [(when nation [:= :nation nation])
                 (when state [:= :state state])
                 (when county [:= :county county])
                 (when city [:= :city city])
                 (when district [:= :district district])
                 (when place-id [:= :place-id place-id])]
        page' (or page 1)
        per' (or per 30)
        where (into [:and] (remove nil? filters))
        q (merge {:select [:*]
                  :from :post
                  :limit per'
                  :offset (* per' (dec page'))}
                 (when (seq where) {:where where}))]
    (db.base/run q)))
