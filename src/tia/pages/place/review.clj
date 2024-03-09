(ns tia.pages.place.review
  (:require
   [clojure.string :as cstr]
   [clojure.tools.logging :as log]
   [malli.core :as m]
   [tia.calc :as c]
   [tia.data :as d]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.file :as db-file]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.model :as model]
   [tia.pages.place.common :as place-common]
   [tia.storage :as storage]
   [tia.util :as u]))

(defn uri [handle elems]
  (-> [c/path :place handle :reviews elems]
      flatten eval))

(def make-page
  (partial place-common/paginate :reviews))

(defn write-review-section [{:keys [place] :as _req}]
  (let [handle (:place/handle place)
        path (uri handle [:create :submit])]
    [:form.container.mt-5.px-5
     {:method :post :action path
      :enctype "multipart/form-data"}
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
     [:input {:type :file :id :file
              :name :file :multiple true
              :accept ".jpg, .jpeg, .png"}]
     [:button.btn.btn-warning "Cancel"]
     [:button.btn.btn-primary {:type :submit} "Submit"]]))

(defn store-file
  [post-id {:keys [filename tempfile size]}]
  (let [file #:file{:id (u/uuid)
                    :objk (str (u/uuid))
                    :kind :image
                    :post-id post-id
                    :designation filename
                    :size size}]
    (if (m/validate model/file file)
      (storage/upload-file file tempfile)
      (log/error "invalid file attempted to store"))))

(defn submit-review-and-redirect-section
  [{:keys [params person place]}]
  (let [{:keys [title detail file]} params
        handle (:place/handle place)
        place-id (db-place/place-handle->id handle)
        post-id (u/uuid)
        post #:post{:id post-id
                    :title title
                    :kind :review
                    :detail detail
                    :place-id place-id
                    :created (u/now)
                    :updated (u/now)
                    :person-id (get person :person/id)}
        path (c/path :place handle :reviews)]
    (db-post/create! post)
    (doseq [item (flatten [file])]
      (store-file post-id item))
    [:div
     [:p "Your review has been posted."]
     [:a {:href path}
      "Move back to review section!"]]))

(defn commentary-card
  [handle user-person-id post-id commentary]
  (let [{:commentary/keys [id updated person-id content]} commentary
        commenter-person-id person-id
        {:person/keys [nickname]} (db-common/pull-by-id commenter-person-id)
        header (str "By " nickname " at " updated)
        deletion-confirm-msg "Do you wish to delete this comment?"
        path (uri handle [post-id :commentaries id :delete])]
    [:form.card {:hx-get path
                 :hx-confirm deletion-confirm-msg
                 :hx-trigger :submit
                 :hx-swap :outerHTML}
     [:div.card-header header]
     [:div.card-body
      [:blockquote.blockquote.mb-0
       [:p content]]]
     (when (= user-person-id commenter-person-id)
       [:div.card-footer
        [:button.btn.btn-link "Edit"]
        [:button.btn.btn-link
         {:type :submit} "Delete"]])]))

(defn post-card [handle user-person-id post-id]
  (let [{:post/keys [title cover detail person-id]}
        (db-common/pull-by-id post-id)
        reviewer-person-id person-id
        {:person/keys [nickname]}
        (db-common/pull-by-id reviewer-person-id)
        images (db-file/get-files-by-post-id post-id)
        presigned-urls (map #(storage/presign-url (:file/objk %)) images)
        detail' (case cover
                  :removed "removed by user"
                  :banned "content is banned"
                  detail)]
    [:div.card {:style "width: 18rem;"}
     [:div.card-body
      [:h5.card-title title]
      [:h6.card-subtitle.mb-2.text-body-secondary
       (str "by " nickname)]
      [:div {:id :review-detail-and-controls}
       [:div (for [purl presigned-urls]
               [:img {:src purl}])]
       [:p {:id :reviewdetail
            :class (c/>s :card-text)
            :style {:white-space :pre-line}}
        detail']
      (when (= reviewer-person-id user-person-id)
        [:button
        {:hx-get (uri handle [post-id :edit-content])
         :hx-confirm "Only the content of the review will be delete. Do you wish to?"
         :hx-target :#review-detail-and-controls
         :hx-trigger :click
         :hx-swap :outerHTML}
        "edit"]
       [:button
        {:hx-delete (uri handle [post-id :delete-content])
         :hx-confirm "Only the content of the review will be delete. Do you wish to?"
         :hx-target :#reviewdetail
         :hx-trigger :click
         :hx-swap :outerHTML}
        "delete"])]]]))

(defn review-section
  [{:keys [place path-params person]}]
  (let [user-person-id (:person/id person)
        handle (:place/handle place)
        post-id (-> path-params :post-id parse-uuid)
        commentaries (db-commentary/get-all-of-post post-id)
        post-path (uri handle [post-id :commentaries :create])]
    [:div.container.mt-5.px-5
     (post-card handle user-person-id post-id)
     [:div#commentaries
      (for [commentary commentaries]
        (commentary-card
         handle user-person-id post-id commentary))]
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
    (l/elem nil)))

(defn post-link [{:keys [handle post]}]
  (let [{:post/keys [id title person-id created]} post
        post-id id
        {:person/keys [nickname]} (db-common/pull-by-id person-id)
        {:keys [latest-commentary-updated
                latest-commentary-commenter-nickname]}
        (db-commentary/get-latest-of-post post-id)
        num-of-commentaries (db-commentary/count-by-post post-id)
        path (uri handle [post-id :read])]
    [:a {:href path
         :class (c/>s :list-group-item :list-group-item-action
                      :d-flex :justify-content-between
                      :align-items-center :py-3)}
     [:div.me-auto
      [:div.fw-bold title]
      [:span.text-body-secondary
       (cstr/join
        " "
        (concat ["Posted by " nickname
                 "at" created]
                (when latest-commentary-commenter-nickname
                  ["Last commented by"
                   latest-commentary-commenter-nickname
                   "at" latest-commentary-updated])))]]
     [:div
      [:div d/chat-empty-icon num-of-commentaries]
      [:div d/heart-outlined-icon "33"]
      [:div d/hand-thumbs-up-outlined-icon "22"]
      [:div d/hand-thumbs-down-outlined-icon "-2"]]]))

(defn reviews-section [{:keys [place]}]
  (let [handle (:place/handle place)
        path (uri handle [:create :write])
        posts (db-post/get-by-handle handle :review)]
    [:div
     [:a.btn.btn-primary
      {:href path} "Write review"]
     [:div.list-group
      (for [post posts]
        (post-link {:handle handle
                      :post post}))]]))

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
    (if (db-commentary/create! commentary)
      (l/elem
       [:div
        [:p nickname]
        [:p content]])
      (l/elem
       [:p "failed in creating comment. refresh page and try again."]))))

(defn remove-detail-and-comp [{:keys [path-params]}]
  (let [msg d/content-deletion-msg
        post-id (-> path-params :post-id parse-uuid)
        post (db-common/pull-by-id post-id)
        post' (assoc post :post/cover :removed)]
    (if (db-common/upsert! post')
      (l/elem [:span msg])
      (l/elem [:span
               "Could not review detail content. Contact administrator."
               (:post/detail post)]))))

(defn edit-content-form
  [{:keys [path-params place]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :post-id parse-uuid)
        post (db-common/pull-by-id post-id)]
    (l/elem
     [:form {:id :review-detail-and-controls
             :hx-patch (uri handle [post-id :edit-content])
             :hx-target :#review-detail-and-controls
             :hx-trigger :submit
             :hx-swap :outerHTML}
      [:textarea {:type :text
                  :name :detail
                  :rows 10}
       (:post/detail post)]
      [:a {:href (uri (:place/handle place)
                      [post-id :read])}
       "cancel"]
      [:button {:type :submit}
       "submit"]])))

(defn patch-detail-comp
  [{:keys [path-params params]}]
  (let [post-id (-> path-params :post-id parse-uuid)
        post (db-common/pull-by-id post-id)
        new-detail (:detail params)
        post' (assoc post :post/detail new-detail)]
    (db-common/upsert! post')
    (l/elem [:p new-detail])))

(def routes
  ["/reviews"
   [["" {:get (make-page reviews-section)}]
    ["/create"
     [["/write" {:get (make-page write-review-section)}]
      ["/submit" {:post (make-page submit-review-and-redirect-section)}]]]
    ["/:post-id"
     [["/read" {:get (make-page review-section)}]
      ["/edit-content" {:get edit-content-form
                        :patch patch-detail-comp}]
      ["/delete-content" {:delete remove-detail-and-comp}]
      ["/commentaries"
       [["/create" {:post create-commentary-comp}]
        ["/:commentary-id"
         [["/edit" {:get delete-commentary-comp-empty}]
          ["/delete" {:get delete-commentary-comp-empty}]]]]]]]]])
