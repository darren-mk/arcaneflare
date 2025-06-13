(ns arcaneflare.database.post.root
  (:require
   [arcaneflare.database.base :as db.base]))

(defn upsert!
  [{:post/keys [id author-id title body kind
                place-id geo-id parent-id is-deleted]}]
  (let [q {:insert-into :post
           :columns [:id :author-id :title :body :kind :place-id
                     :geo-id :parent-id :is-deleted :updated-at]
           :values [[id author-id title body kind place-id
                     geo-id parent-id (or is-deleted false) [:raw "now()"]]]
           :on-conflict [:id]
           :do-update-set {:author-id   :excluded.author_id
                           :title       :excluded.title
                           :body        :excluded.body
                           :kind        :excluded.kind
                           :place-id    :excluded.place_id
                           :geo-id      :excluded.geo_id
                           :parent-id   :excluded.parent_id
                           :is-deleted  :excluded.is_deleted
                           :updated-at  :excluded.updated_at}}]
    (db.base/run q)))

(defn single-by [{:post/keys [id]}]
  (db.base/run
    {:select [:*]
     :from [:post]
     :where [:= :id id]}))

(defn multi-by
  [{:post/keys [author-id title body kind place-id
                geo-id parent-id is-deleted page per]}]
  (let [wrap #(str "%" % "%")
        filters [(when author-id [:= :author-id author-id])
                 (when title [:ilike :title (wrap title)])
                 (when body [:ilike :body (wrap body)])
                 (when kind [:= :kind kind])
                 (when place-id [:= :place-id place-id])
                 (when geo-id [:= :geo-id geo-id])
                 (when parent-id [:= :parent-id parent-id])
                 (when is-deleted [:= :is-deleted is-deleted])]
        page' (or page 1)
        per' (or per 30)
        where (into [:and] (filter identity filters))
        q (merge {:select [:*]
                  :from :post
                  :limit per'
                  :offset (* per' (dec page'))}
                 (when (seq where) {:where where}))]
    (db.base/run q)))
