(ns tia.pages.place
  (:require
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.calc :refer [>s]]
   [tia.components.post :as cpost]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
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
     (cpost/root {:industry :strip-club
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
    (l/frame {}
             [:p "yoloyolo"])))

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

(defn reading [{:keys [path-params]}]
  (let [postid (-> path-params :postid parse-uuid)
        {:post/keys [detail]} (dbc/pull-by-id postid)]
    (l/frame
     {}
     [:div
      [:p detail]])))

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
     ["/item/:postid" {:get reading}]]]
   ["/gallery" {:get (page :gallery)
                :post upload}]])
