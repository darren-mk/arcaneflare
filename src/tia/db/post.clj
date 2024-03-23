(ns tia.db.post
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.calc :as c]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn locate [m]
  (if (:place-id m)
    (update m :place-id parse-uuid)
    m))

(comment
  (locate {:place-id "48e5e179-760c-4a80-b8ba-6b8a03210ef9"})
  :=> {:place-id #uuid "48e5e179-760c-4a80-b8ba-6b8a03210ef9"}
  (locate {:city "Hackensack"})
  :=> {:city "Hackensack"})

(defn coerce [m]
  (-> m c/kebab-m
      (update :subject keyword)
      (update :curb keyword)
      (update :location dbc/->edn)
      (update :location locate)
      (u/map->nsmap :post)))

(u/mf coerce [:map model/post])

(defn get-all []
  (let [q {:select [:*]
           :from [:post]}]
    (map coerce (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:post{:curb :none,
               :edited-at #inst "2024-03-23T16:31:22.913000000-00:00",
               :title "Sample happy time at sample place",
               :id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62",
               :author-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
               :created-at #inst "2024-03-23T16:31:22.913000000-00:00",
               :location {:place-id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"},
               :subject :review,
               :detail "twas really good time! yay! foo!"}))

(defn create!
  [{:post/keys [id subject curb author-id location title
                detail created-at edited-at] :as post}]
  (assert (m/validate model/post post))
  (dbc/hd {:insert-into [:post]
           :columns [:id :subject :curb :author-id :location
                     :title :detail :created-at :edited-at]
           :values [[id (name subject) (name curb) author-id
                     (dbc/->jsonb location) title detail
                     created-at edited-at]]}))

(comment
  (create! #:post{:id (u/uuid)
                  :subject :review
                  :curb :none
                  :author-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f"
                  :location {:place-id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"}
                  :title "Sample happy time at sample place"
                  :detail "twas really good time! yay! foo!"
                  :created-at (u/now)
                  :edited-at (u/now)})
  :=> nil)

(defn get-by-handle-and-kind [handle kind]
  (let [raw "(post.location->>'place-id')::uuid"
        qr {:select [:post.*]
            :from [:post]
            :join [:place [:= :place.id [:raw raw]]]
            :where [[:= :place.handle (name handle)]]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-by-handle-and-kind :johnny-as-hitching-post :review)
  :=> '(#:post{:curb :none,
               :edited-at #inst "2024-03-23T16:31:22.913000000-00:00",
               :title "Sample happy time at sample place",
               :id #uuid "f4632b70-5db5-431c-87f9-2fce82ff3e62",
               :author-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
               :created-at #inst "2024-03-23T16:31:22.913000000-00:00",
               :location
               {:place-id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"},
               :subject :review,
               :detail "twas really good time! yay! foo!"}))
