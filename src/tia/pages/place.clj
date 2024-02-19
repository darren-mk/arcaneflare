(ns tia.pages.place
  (:require
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.calc :refer [>s]]
   [tia.components.write :as comp-write]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as dbc]
   [tia.layout :as l]
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
  (let [images (dbc/pull-all-having-kv :image/place-id id)
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
        {:person/keys [nickname]} (dbc/pull-by-id person-id)]
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

(defn write [{:keys [path-params]}]
  (let [handle (-> path-params :handle keyword)]
    (l/frag
     (comp-write/root {:industry :strip-club
                  :handle handle}))))

(defn book [{:keys [params person path-params]}]
  (let [{:keys [title detail]} params
        handle (-> path-params :handle keyword)
        place-id (db-place/place-handle->id handle)
        post #:post{:id (u/uuid)
                    :title title
                    :kind :review
                    :detail detail
                    :place-id place-id
                    :created (u/now)
                    :updated (u/now)
                    :person-id (get person :person/id)}]
    (dbc/record! post)
    (l/frame
     {}
     [:div
      [:p "Your review has been posted."]
      [:a {:href (cstr/join "/" [uri (name handle) "review"])}
       "Move back to review section!"]])))

(defn review [{:keys [_place handle]}]
  (let [path (cstr/join "/" [uri handle "review" "write"])
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

(defn single-review-page [{:keys [path-params] :as _req}]
  (let [handle (-> path-params :handle)
        post-id (-> path-params :postid parse-uuid)
        {:post/keys [title detail person-id]} (dbc/pull-by-id post-id)
        {:person/keys [nickname]} (dbc/pull-by-id person-id)
        commentaries (db-commentary/get-by-post-id post-id)]
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
         [:p (:commentary/content commentary)])]
      [:button.btn.btn-primary
       {:id :write-commentary-button
        :hx-get (cstr/join "/" [uri (name handle) "review" "item"
                                (str post-id) "write-commentary"])
        :hx-target "#write-commentary-button"
        :hx-trigger "click"
        :hx-swap "outerHTML"}
       "Comment"]])))

(defn record-commentary-then-single-review-page
  [{:keys [path-params params person] :as _req}]
  (let [commentary-content (:content params)
        commentary-author-id (:person/id person)
        handle (-> path-params :handle keyword)
        post-id (-> path-params :postid parse-uuid)
        commentary-id (u/uuid)
        commentary #:commentary{:id commentary-id
                                :content commentary-content
                                :created (u/now)
                                :updated (u/now)
                                :post-id post-id
                                :person-id commentary-author-id}]
    (dbc/record! commentary)
    (if (u/retry {:interval 50
                  :max 10
                  :f #(dbc/pull-by-id commentary-id)})
      {:status 301
       :headers {"Location" (cstr/join "/" [uri (name handle) "review" "item" post-id])}}
      {})))

(defn write-commentary [{:keys [path-params]}]
  (let [handle (-> path-params :handle keyword)
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

(defn submit-commentary [_req]
  (println "do something!")
  (l/frag
   [:p 123]))

(defn content
  [{:keys [selection place address handle]}]
  (case selection
    :info (info place address)
    :event [:h1 "event will be here."]
    :menu [:h1 "menu will be here."]
    :dancer [:h1 "dancer will be here."]
    :review (review {:place place
                     :handle handle})
    :gallery (gallery place)))

(defn tab [ident selection handle]
  (let [elems ["/place" (name handle) (name ident)]
        href (cstr/join "/" elems)
        label (-> ident name cstr/capitalize)
        selected? (= ident selection)]
    [:li.nav-item
     [:a.nav-link {:href href
                   :class (if selected? "active" nil)
                   :aria-current selected?}
      label]]))

(defn tabs [selection handle]
  (let [f #(tab % selection handle)
        elems (mapv f selections)]
    (into [:ul.nav.nav-tabs]
          elems)))

(m/=> page
      [:=> [:cat selections-enum]
       :any])

(defn page [selection]
  (fn [{:keys [session] :as req}]
    (let [handle (-> req :path-params
                     :handle keyword)
          {:keys [place address]}
          (db-place/find-place-and-address handle)]
      (l/frame
       {:nav {:selection :club}
        :session session}
       [:div.container-md.px-3.px-sm-4.px-xl-5
        [:div.row
         [:div.py-3.py-sm-4
          [:h1.h3.lh-base.mb-1 (get place :place/label)]
          (tabs selection handle)
          (content {:selection selection
                    :place place
                    :address address
                    :handle handle})]]]))))

(defn upload [{:keys [person path-params] :as req}]
  (let [person-id (:person/id person)
        handle (-> path-params :handle keyword)
        place-id (db-place/place-handle->id handle)
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
  [(str uri "/:handle")
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
       ]]]]
   ["/gallery" {:get (page :gallery)
                :post upload}]])
