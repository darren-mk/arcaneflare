(ns tia.db.post
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
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

(defn translate [m]
  (let [renaming {:author_id :author-id
                  :created_at :created-at
                  :edited_at :edited-at}
        post (-> (cset/rename-keys m renaming)
                 (update :subject keyword)
                 (update :curb keyword)
                 (update :location dbc/->edn)
                 (update :location locate)
                 (u/map->nsmap :post))]
    (m/coerce model/post post)))

(defn get-all []
  (let [q {:select [:*]
           :from [:post]}]
    (map translate (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:post{:curb :none,
               :edited-at #inst "2024-03-14T13:39:08.273000000-00:00",
               :title "Sample happy time at sample place",
               :id #uuid "d43d0d0c-f4e2-475f-b891-888449a41425",
               :author-id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c",
               :created-at #inst "2024-03-14T13:39:08.273000000-00:00",
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
                  :author-id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c"
                  :location {:place-id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"}
                  :title "Sample happy time at sample place"
                  :detail "twas really good time! yay! foo!"
                  :created-at (u/now)
                  :edited-at (u/now)})
  :=> nil)

(defn get-by-handle [handle kind]
  (let [qr '{:find [(pull ?post [*])]
             :in [[?handle ?kind]]
             :where [[?place :place/handle ?handle]
                     [?place :place/id ?place-id]
                     [?post :post/place-id ?place-id]
                     [?post :post/subject ?kind]]}]
    (map first (dbc/query qr [handle kind]))))

(comment
  (get-by-handle :silk-gentlemens-club :review))
