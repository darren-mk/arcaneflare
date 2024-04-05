(ns tia.db.commentary
  (:require
   [malli.core :as m]
   [tia.calc :as c]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn coerce [m]
  (-> m c/kebab-m 
      (u/map->nsmap :commentary)))
(u/mf coerce [:map model/commentary])

(defn get-all []
  (let [q {:select [:*]
           :from [:commentary]}]
    (map coerce (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:commentary{:id #uuid "2ef2a9e0-9ab7-408a-9153-ea019b96713b",
                     :post-id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62",
                     :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                     :content "yes, agreed. good place to have happy time!",
                     :created-at #inst "2024-03-23T17:59:12.922000000-00:00",
                     :edited-at #inst "2024-03-23T17:59:12.922000000-00:00"}))

(defn create!
  [{:commentary/keys [id post-id person-id content
                      created-at edited-at] :as commentary}]
  (assert (m/validate model/commentary commentary))
  (dbc/hd {:insert-into [:commentary]
            :columns [:id :post_id :person_id :content
                      :created-at :edited-at]
            :values [[id post-id person-id content
                      created-at edited-at]]}))

(comment
  (create!
   #:commentary{:id (u/uuid)
                :post-id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62"
                :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f"
                :content "yes, agreed. good place to have happy time!"
                :created-at (u/now)
                :edited-at (u/now)})
  :=> nil)

(defn get-by-post-id [post-id]
  (let [qr {:select [:commentary.*]
            :from [:commentary]
            :join [:post [:= :commentary.post-id :post.id]]
            :where [[:= :commentary.post-id post-id]]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-by-post-id
   #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62"))

(defn get-latest-by-post-id [post-id]
  (let [qr {:select [:commentary.*]
            :from [:commentary]
            :join [:post [:= :commentary.post-id :post.id]]
            :where [[:= :commentary.post-id post-id]]
            :order-by [[:commentary.created-at :desc]]
            :limit 1}]
    (map coerce (dbc/hq qr))))

(comment
  (get-latest-by-post-id
   #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62"))

(defn count-by-post-id [post-id]
  (let [qr {:select [:%count.*]
            :from [:commentary]
            :join [:post [:= :commentary.post-id :post.id]]
            :where [[:= :commentary.post-id post-id]]}]
    (-> qr dbc/hq first :count)))

(comment
  (count-by-post-id
   #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62")
  :=> 1)

(defn delete-by-id! [id]
  (let [qr {:delete [:commentary]
            :where [[:= :id id]]}]
    (-> qr dbc/hq first :count)))
