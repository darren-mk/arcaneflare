(ns tia.pages.place
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.calc :refer [>s]]
   [tia.components.inputs :as comp-input]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.middleware :as mw]
   [tia.storage :as storage]
   [tia.util :as u]))

(def uri
  "/place")

(def selections
  [:info :event :menu
   :dancer :review :gallery])

(def selections-enum
  (into [:enum] selections))

(defn info
  [{:place/keys [label nudity]}
   {:address/keys [street city state country]}]
  [:div
   [:p label]
   [:p (cstr/join " " ["Nudity: " nudity])]
   [:p (cstr/join " " ["Address: " street city state country])]])

(defn gallery [{:keys [place/id] :as place}]
  (let [images (db-common/pull-all-having-kv :image/place-id id)
        presigned-urls (map #(storage/presign-url (:image/objk %)) images)]
    [:div
     [:h1 "images will be here."]
     [:h2 (str place)]
     [:h3 (str (first images))]
     [:div
      (for [purl presigned-urls]
        [:img {:src purl}])]
     [:form {:method :post
             :enctype "multipart/form-data"}
      [:div
       [:label {:for :file}
        "choose file"]
       [:input {:type :file
                :id :file
                :name :file
                :multiple true
                :accept ".jpg, .jpeg, .png"}]]
      [:button
       "upload image"]]]))

(defn render-post [{:keys [handle post]}]
  (let [{:post/keys [id title person-id]} post
        {:person/keys [nickname]} (db-common/pull-by-id person-id)]
    [:a {:href (cstr/join "/" [uri (name handle) "review" "item" (str id)])
         :class (>s :list-group-item :list-group-item-action
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

(defn write [{:keys [place]}]
  (let [handle (:place/handle place)]
    (l/frag
     (comp-input/root {:industry :strip-club
                       :handle handle}))))

(defn book [{:keys [params person path-params place]}]
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
                    :person-id (get person :person/id)}]
    (db-common/record! post)
    (l/frame
     {}
     [:div
      [:p "Your review has been posted."]
      [:a {:href (cstr/join "/" [uri (name handle) "review"])}
       "Move back to review section!"]])))

(defn review [{:place/keys [handle] :as _place}]
  (let [path (cstr/join "/" [uri (name handle) "review" "write"])
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

(defn confirm-delete-commentary [{:keys [place path-params]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (-> path-params :id parse-uuid)]
    (l/frag
     [:form
      {:method :post
       :action (cstr/join "/" [uri (name handle) "review" "item"
                               (str post-id) "delete-commentary" commentary-id])}
      [:p "Are you sure to delete?"]
      [:button "Cancel"]
      [:button "Yes, Delete"]])))

(defn commentary-card
  [handle post-id {:commentary/keys [id updated person-id content]}]
  (let [{:person/keys [nickname]} (db-common/pull-by-id person-id)
        header (str "By " nickname " at " updated)]
    [:div.card
     [:div.card-header header]
     [:div.card-body
      [:blockquote.blockquote.mb-0
       [:p content]]]
     [:div.card-footer
      [:button.btn.btn-link "Edit"]
      [:button.btn.btn-link
       {:id :write-commentary-button
        :hx-get (cstr/join "/" [uri (name handle) "review" "item"
                                (str post-id) "confirm-delete-commentary" id])
        :hx-trigger "click"
        :hx-swap "outerHTML"}
       "Delete"]]]))

(defn single-review-page [{:keys [place path-params] :as _req}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        {:post/keys [title detail person-id]} (db-common/pull-by-id post-id)
        {:person/keys [nickname]} (db-common/pull-by-id person-id)
        commentaries (db-commentary/get-commentaries-by-post-id post-id)]
    (l/frame
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
        :hx-get (cstr/join "/" [uri (name handle) "review" "item"
                                (str post-id) "write-commentary"])
        :hx-trigger "click"
        :hx-swap "outerHTML"}
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
                                :person-id commentary-author-id}]
    (db-common/record! commentary)
    (if (u/retry-check-existence
         {:interval 80 :max 10
          :f #(db-common/pull-by-id commentary-id)})
      {:status 301
       :headers {"Location" (cstr/join "/" [uri (name handle) "review" "item" post-id])}}
      {})))

(defn write-commentary [{:keys [place path-params]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)]
    (l/frag
     [:form.container.mt-5.px-5
      {:action (cstr/join "/" [uri (name handle) "review" "item"
                               (str post-id)])
       :method "post"}
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

(defn delete-commentary [{:keys [path-params place]}]
  (let [handle (:place/handle place)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (-> path-params :id parse-uuid)]
    (db-common/delete! commentary-id)
    (u/retry-check-deletion
     {:interval 100 :max 10 :check some?
      :f #(db-common/pull-by-id commentary-id)})
    {:status 301
     :headers {"Location" (cstr/join "/" [uri (name handle) "review" "item" post-id])}}))

(defn submit-commentary [_req]
  (println "do something!")
  (l/frag
   [:p 123]))

(defn content
  [selection place address]
  (case selection
    :info (info place address)
    :event [:h1 "event will be here."]
    :menu [:h1 "menu will be here."]
    :dancer [:h1 "dancer will be here."]
    :review (review place)
    :gallery (gallery place)))

(defn tab [ident selection place]
  (let [handle (:place/handle place)
        elems ["/place" (name handle) (name ident)]
        href (cstr/join "/" elems)
        label (-> ident name cstr/capitalize)
        selected? (= ident selection)]
    [:li.nav-item
     [:a.nav-link {:href href
                   :class (if selected? "active" nil)
                   :aria-current selected?}
      label]]))

(defn tabs [selection place]
  (let [f #(tab % selection place)
        elems (mapv f selections)]
    (into [:ul.nav.nav-tabs]
          elems)))

(m/=> page
      [:=> [:cat selections-enum]
       :any])

(defn page [selection]
  (fn [{:keys [session place address] :as _req}]
    (l/frame
     {:nav {:selection :club}
      :session session}
     [:div.container-md.px-3.px-sm-4.px-xl-5
      [:div.row
       [:div.py-3.py-sm-4
        [:h1.h3.lh-base.mb-1 (:place/label place)]
        (tabs selection place)
        (content selection place address)]]])))

(defn upload [{:keys [person place] :as req}]
  (let [person-id (:person/id person)
        place-id (:place/id place)
        {:keys [filename tempfile size]}
        (-> req :params :file)
        image #:image{:id (u/uuid)
                      :objk (str (u/uuid))
                      :place-id place-id
                      :person-id person-id
                      :filename filename
                      :size size}]
    (storage/upload-image
     image tempfile)))

(def routes
  [(str uri "/:handle") {:middleware [mw/handle->place+address]}
   ["/info" {:get (page :info)}]
   ["/event" {:get (page :event)}]
   ["/menu" {:get (page :menu)}]
   ["/dancer" {:get (page :dancer)}]
   ["/review"
    [["" {:get (page :review)
          :post book}]
     ["/write" {:get write}]
     ["/item/:postid"
      [["" {:get single-review-page
            :post record-commentary-then-single-review-page}]
       ["/write-commentary" {:get write-commentary}]
       ["/submit-commentary" {:post submit-commentary}]
       ["/confirm-delete-commentary/:id" {:get confirm-delete-commentary}]
       ["/delete-commentary/:id" {:post delete-commentary}]]]]]
   ["/gallery" {:get (page :gallery)
                :post upload}]])
