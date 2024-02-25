(ns tia.pages.place.review
  (:require
   [tia.calc :as c]
   [tia.components.inputs :as comp-input]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.pages.place.common :as p-common]
   [tia.util :as u]))

(defn record-review-and-confirm-page
  [{:keys [params person place]}]
  (let [{:keys [title detail]} params
        handle (:place/handle place)
        place-id (db-place/place-handle->id handle)
        post #:post{:id (u/uuid)
                    :title title
                    :kind :review
                    :detail detail
                    :place-id place-id
                    :created (u/now)
                    :updated (u/now)
                    :person-id (get person :person/id)}
        path (c/path :place handle :reviews)]
    (db-common/record! post)
    (l/page
     {}
     [:div
      [:p "Your review has been posted."]
      [:a {:href path}
       "Move back to review section!"]])))

(defn write [{:keys [place]}]
  (let [handle (:place/handle place)]
    (l/comp
     (comp-input/root {:industry :strip-club
                       :handle handle}))))

(defn commentary-card
  [handle post-id {:commentary/keys [id updated person-id content]}]
  (let [{:person/keys [nickname]} (db-common/pull-by-id person-id)
        header (str "By " nickname " at " updated)
        path (c/path :place handle :reviews post-id
                     :confirm-delete-commentary id)]
    [:div.card
     [:div.card-header header]
     [:div.card-body
      [:blockquote.blockquote.mb-0
       [:p content]]]
     [:div.card-footer
      [:button.btn.btn-link "Edit"]
      [:button.btn.btn-link
       {:id :write-commentary-button
        :hx-get path
        :hx-trigger "click"
        :hx-swap "outerHTML"}
       "Delete"]]]))

(defn single-review-page
  [{:keys [place path-params] :as _req}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        {:post/keys [title detail person-id]} (db-common/pull-by-id post-id)
        {:person/keys [nickname]} (db-common/pull-by-id person-id)
        commentaries (db-commentary/get-commentaries-by-post-id post-id)
        path (c/path :place handle :reviews
                     post-id :write-commentary)]
    (l/page
     {}
     [:div.container.mt-5.px-5
      [:div.card {:style "width: 18rem;"}
       [:div.card-body
        [:h5.card-title title]
        [:h6.card-subtitle.mb-2.text-body-secondary
         (str "by " nickname)]
        [:p.card-text
         {:style {:white-space :pre-line}}
         detail]]]
      [:div
       (for [commentary commentaries]
         (commentary-card handle post-id commentary))]
      [:button.btn.btn-primary
       {:id :write-commentary-button
        :hx-get path
        :hx-trigger :click
        :hx-swap :outerHTML}
       "Comment"]])))

(defn record-commentary-then-single-review-page
  [{:keys [path-params params person place] :as _req}]
  (let [commentary-content (:content params)
        commentary-author-id (:person/id person)
        handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (u/uuid)
        commentary #:commentary{:id commentary-id
                                :content commentary-content
                                :created (u/now)
                                :updated (u/now)
                                :post-id post-id
                                :person-id commentary-author-id}
        path (c/path :place handle :reviews post-id)]
    (db-common/record! commentary)
    (when (u/retry-check-existence
           {:interval 80 :max 10
            :f #(db-common/pull-by-id commentary-id)})
      {:status 301
       :headers {"Location" path}})))

(defn write-commentary-form
  [{:keys [place path-params]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        path (c/path :place handle :reviews post-id)]
    (l/comp
     [:form.container.mt-5.px-5
      {:action path :method :post}
      [:div.input-group.my-3
       [:span.input-group-text
        {:name :content}
        "Comment"]
       [:textarea.form-control
        {:type :text :name :content
         :placeholder "type here"
         :rows 10}]]
      [:button.btn.btn-warning
       "Cancel"]
      [:button.btn.btn-primary
       {:type :submit}
       "Submit"]])))

(defn delete-commentary-and-redirect
  [{:keys [path-params place]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (-> path-params :id parse-uuid)]
    (db-common/delete! commentary-id)
    (u/retry-check-deletion
     {:interval 100 :max 10 :check some?
      :f #(db-common/pull-by-id commentary-id)})
    {:status 301
     :headers {"Location" (c/path :place handle :reviews post-id)}}))

(defn confirm-delete-commentary-form [{:keys [place path-params]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (-> path-params :id parse-uuid)
        path (c/path :place handle :reviews post-id
                     :delete-commentary commentary-id)]
    (l/comp
     [:form
      {:method :post
       :action path}
      [:p "Are you sure to delete?"]
      [:button "Cancel"]
      [:button "Yes, Delete"]])))

(defn render-post [{:keys [handle post]}]
  (let [{:post/keys [id title person-id]} post
        {:person/keys [nickname]} (db-common/pull-by-id person-id)
        path (c/path :place handle :reviews id)]
    [:a {:href path
         :class (c/>s :list-group-item :list-group-item-action
                    :d-flex :justify-content-between
                    :align-items-center :py-3)}
     [:div.me-auto
      [:div.fw-bold title]
      [:span.text-body-secondary (str "authored by " nickname)]]
     [:div
      [:div "Comments: 23"]
      [:span.text-body-secondary "Views: 280"]]]))

(defn listing [{:keys [posts handle]}]
  [:div.list-group
   (for [post posts]
     (render-post {:handle handle
                   :post post}))])

(defn review-page [{:place/keys [handle] :as _place}]
  (let [path (c/path :place handle :reviews :components :write)
        review-posts (db-post/get-by-handle handle)]
    [:div#reviewparent
     [:button.btn.btn-primary
      {:hx-get path
       :hx-target "#reviewparent"
       :hx-trigger "click"
       :hx-swap "outerHTML"}
      "Write"]
     (listing {:handle handle
               :posts review-posts})]))

(def routes
  ["/reviews" 
   [["" {:get (p-common/paginate :reviews review-page)
         :post record-review-and-confirm-page}]
    ["/components/write" {:get write}]
    ["/:postid"
     [["" {:get single-review-page
           :post record-commentary-then-single-review-page}]
      ["/write-commentary" {:get write-commentary-form}]
      ["/confirm-delete-commentary/:id" {:get confirm-delete-commentary-form}]
      ["/delete-commentary/:id" {:post delete-commentary-and-redirect}]]]]])
