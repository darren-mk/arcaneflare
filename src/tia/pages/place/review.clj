(ns tia.pages.place.review
  (:require
   [tia.calc :as c]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.pages.place.common :as place-common]
   [tia.util :as u]))

(defn uri [handle elems]
  (-> [c/path :place handle :reviews elems]
      flatten eval))

(defn write-review-section [{:keys [place] :as _req}]
  (let [handle (:place/handle place)
        path (uri handle [:create :confirm])]
    [:form.container.mt-5.px-5
     {:method :get :action path}
     [:div.input-group.my-3
      [:span.input-group-text "Title"]
      [:input.form-control
       {:type :text
        :name :title
        :placeholder "Write title of your review"}]]
     [:div.input-group.my-3
      [:span.input-group-text {:id "add-on-2"} "Content"]
      [:textarea.form-control
       {:type :text
        :name :detail
        :placeholder "Write content of your review"
        :rows 10
        :aria-label "Description"
        :aria-describedby "add-on-2"}]]
     [:button.btn.btn-warning "Cancel"]
     [:button.btn.btn-primary {:type :submit} "Submit"]]))

(defn confirm-write-section [{:keys [place params]}]
  (let [{:keys [title detail]} params
        handle (:place/handle place)
        path (uri handle [:create :redirect])]
     [:form.container.mt-5.px-5
     {:method :get :action path}
     [:div.input-group.my-3
      [:span.input-group-text "Title"]
      [:input.form-control
       {:type :text :name :title :value title
        :placeholder "Write title of your review"}]]
     [:div.input-group.my-3
      [:span.input-group-text {:id "add-on-2"} "Content"]
      [:input.form-control
       {:type :text :name :detail :value detail
        :placeholder "Write content of your review"
        :rows 10 :aria-label "Description"
        :aria-describedby "add-on-2"}]]
     [:button.btn.btn-warning "Cancel"]
     [:button.btn.btn-primary {:type :submit} "Submit"]]))

(defn redirect-write-section
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
    [:div
     [:p "Your review has been posted."]
     [:a {:href path}
      "Move back to review section!"]]))

(defn commentary-card
  [handle post-id {:commentary/keys [id updated person-id content]}]
  (let [{:person/keys [nickname]} (db-common/pull-by-id person-id)
        header (str "By " nickname " at " updated)
        path (uri handle [post-id :commentaries id :delete])]
    [:form.card {:hx-get path
                 :hx-trigger :submit
                 :hx-swap :outerHTML}
     [:div.card-header header]
     [:div.card-body
      [:blockquote.blockquote.mb-0
       [:p content]]]
     [:div.card-footer
      [:button.btn.btn-link "Edit"]
      [:button.btn.btn-link
       {:type :submit}
       "Delete"]]]))

(defn review-section
  [{:keys [place path-params] :as _req}]
  (let [handle (:place/handle place)
        post-id (-> path-params :post-id parse-uuid)
        {:post/keys [title detail person-id]} (db-common/pull-by-id post-id)
        {:person/keys [nickname]} (db-common/pull-by-id person-id)
        commentaries (db-commentary/get-commentaries-by-post-id post-id)
        post-path (uri handle [post-id :commentaries :create])]
    [:div.container.mt-5.px-5
     [:div.card {:style "width: 18rem;"}
      [:div.card-body
       [:h5.card-title title]
       [:h6.card-subtitle.mb-2.text-body-secondary
        (str "by " nickname)]
       [:p.card-text
        {:style {:white-space :pre-line}}
        detail]]]
     [:div#commentaries
      (for [commentary commentaries]
        (commentary-card handle post-id commentary))]
     [:form.container.mt-5.px-5
      {:hx-post post-path
       :hx-trigger :submit
       :hx-target :#commentaries
       :hx-swap :beforeend}
      [:div.input-group.my-3
       [:span.input-group-text
        "Comment"]
       [:textarea.form-control
        {:type :text :name :content
         :placeholder "type here"
         :rows 10}]]
      [:button.btn.btn-warning
       "Cancel"]
      [:button.btn.btn-primary
       {:type :submit}
       "Submit"]]]))

(defn delete-commentary-comp-empty [{:keys [path-params]}]
  (let [commentary-id (-> path-params :commentary-id parse-uuid)]
    (db-common/delete! commentary-id)
    (l/comp nil)))

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
        path (uri handle [id :read])]
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

(defn reviews-section [{:keys [place] :as _req}]
  (let [handle (:place/handle place)
        path (uri handle [:create :write])
        posts (db-post/get-by-handle handle)]
    [:div
     [:a.btn.btn-primary
      {:href path} "Write review"]
     [:div.list-group
      (for [post posts]
        (render-post {:handle handle
                      :post post}))]]))

(def make-page
  (partial place-common/paginate :reviews))

(defn create-commentary-comp [{:keys [person params path-params]}]
  (let [post-id (-> path-params :post-id parse-uuid)
        nickname (:person/nickname person)
        content (:content params)
        commentary #:commentary{:id (u/uuid)
                                :content content
                                :created (u/now)
                                :updated (u/now)
                                :post-id post-id
                                :person-id (:person/id person)}]
    (if (db-common/record! commentary)
      (l/comp
       [:div
        [:p nickname]
        [:p content]])
      (l/comp
       [:p "failed in creating comment. refresh page and try again."]))))

(def routes
  ["/reviews"
   [["" {:get (make-page reviews-section)}]
    ["/create"
     [["/write" {:get (make-page write-review-section)}]
      ["/confirm" {:get (make-page confirm-write-section)}]
      ["/redirect" {:get (make-page redirect-write-section)}]]]
    ["/:post-id"
     [["/read" {:get (make-page review-section)}]
      ["/commentaries"
       [["/create" {:post create-commentary-comp}]
        ["/:commentary-id"
         [["/edit" {:get delete-commentary-comp-empty}]
          ["/delete" {:get delete-commentary-comp-empty}]]]]]]]]])
