(ns tia.db.file
  (:require
   [malli.core :as m]
   [tia.calc :as c]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn coerce [m]
  (-> m c/kebab-m
      (u/update-if-exists :kind keyword)
      (u/map->nsmap :file)))

(u/mf coerce [:map model/session])

(defn get-all []
  (let [qr {:select [:*]
            :from [:file]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-all)
  :=> '(#:file{:id #uuid "0eb0dfb3-cd12-4bce-b8ba-0c77185c96cd",
               :post-id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62",
               :kind :image,
               :object-key "abc123xyz456",
               :designation "love.jpg",
               :size 25000,
               :created-at #inst "2024-03-23T19:22:32.119000000-00:00"}))

(defn create!
  [{:file/keys [id post-id kind object-key designation size created-at]
    :as file}]
  (assert (m/validate model/file file))
  (dbc/hd {:insert-into [:file]
           :columns [:id :post_id :kind :object_key
                     :designation :size :created_at]
           :values [[id post-id (name kind) object-key designation size created-at]]}))

(comment
  (create!
   #:file{:id #uuid "cd15a985-59a7-42ca-b948-787fe2ea867e"
          :post-id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62"
          :kind :image
          :object-key "abc123xyz456"
          :designation "love.jpg"
          :size 25000
          :created-at (u/now)}))

(defn get-files-by-post-id [post-id]
  (let [qr {:select [:file.*]
            :from [:file]
            :where [[:= :file.post-id post-id]]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-files-by-post-id
   #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62"))

(defn get-files-by-place-id [place-id]
  (let [raw "(post.location->>'place-id')::uuid"
        qr {:select [:file.*]
            :from [:file]
            :join [:post [:= :file.post-id :post.id]
                   :place [:= :place.id [:raw raw]]]            
            :where [[:= :place.id place-id]]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-files-by-place-id
   #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4")
  :=> '(#:file{:id #uuid "0eb0dfb3-cd12-4bce-b8ba-0c77185c96cd",
               :post-id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62",
               :kind :image,
               :object-key "abc123xyz456",
               :designation "love.jpg",
               :size 25000,
               :created-at #inst "2024-03-23T19:22:32.119000000-00:00"}))

(defn delete-file-by-object-key [object-key]
  (let [qr {:delete-from [:file]
            :where [:= :file.object-key object-key]}]
    (dbc/hd qr)))

(comment
  (delete-file-by-object-key "abc123xyz456")
  :=> nil)
