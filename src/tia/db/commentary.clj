(ns tia.db.commentary
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.db.common :as dbcm]
   [tia.model :as model]
   [tia.util :as u]))

(defn translate [m]
  (let [renaming {:post_id :post-id
                  :annotator_id :annotator-id
                  :created_at :created-at
                  :edited_at :edited-at}
        commentary (-> (cset/rename-keys m renaming)
                       (u/map->nsmap :commentary))]
    (m/coerce model/commentary commentary)))

(defn get-all []
  (let [q {:select [:*]
           :from [:commentary]}]
    (map translate (dbcm/hq q))))

(comment
  (take 1 (get-all))
  :=> (#:commentary{:id #uuid "4ea34bb9-6122-4a58-9dd2-5207d3dc0580",
                    :content "yes, agreed. good place to have happy time!",
                    :post-id #uuid "d43d0d0c-f4e2-475f-b891-888449a41425",
                    :annotator-id #uuid "0cb97a6c-2ed8-44dd-a403-439ab21b93fd",
                    :created-at #inst "2024-03-14T14:09:30.728000000-00:00",
                    :edited-at #inst "2024-03-14T14:09:30.728000000-00:00"}))

(defn create!
  [{:commentary/keys [id post-id annotator-id content
                      created-at edited-at] :as commentary}]
  (assert (m/validate model/commentary commentary))
  (dbcm/hd {:insert-into [:commentary]
            :columns [:id :post_id :annotator_id :content
                      :created-at :edited-at]
            :values [[id post-id annotator-id content
                      created-at edited-at]]}))

(comment
  (create! #:commentary{:id (u/uuid)
                        :post-id #uuid "d43d0d0c-f4e2-475f-b891-888449a41425"
                        :annotator-id #uuid "0cb97a6c-2ed8-44dd-a403-439ab21b93fd"
                        :content "yes, agreed. good place to have happy time!"
                        :created-at (u/now)
                        :edited-at (u/now)})
  :=> nil)

(defn get-all-of-post [post-id]
  (let [qr '{:find [(pull ?commentary [*])]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/edited-at ?updated]]}
        raw (dbcm/query qr [post-id])]
    (->> raw
         (map first)
         (sort-by :commentary/edited-at))))

(defn get-latest-of-post [post-id]
  (let [qr '{:find [?id ?updated ?person-id ?nickname]
             :keys [latest-commentary-id latest-commentary-updated
                    latest-commentary-commenter-id
                    latest-commentary-commenter-nickname]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/id ?id]
                     [?commentary :commentary/annotator-id ?person-id]
                     [?commentary :commentary/edited-at ?updated]
                     [?person :person/id ?person-id]
                     [?person :person/nickname ?nickname]]
             :order-by [[?updated :desc]]
             :limit 1}]
    (first (dbcm/query qr [post-id]))))

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
    (or (ffirst (dbcm/query qr [post-id])) 0)))

(comment
  (count-by-post
   #uuid"2c03a573-9eda-4800-8481-ae5c3f664c00")
  :=> 2)
