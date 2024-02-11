(ns tia.pages.place
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.db.place :as db-place]
   [tia.db.common :as dbc]
   [tia.layout :as layout]
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

(defn content [selection place address]
  (case selection
    :info (info place address)
    :event [:h1 "event will be here."]
    :menu [:h1 "menu will be here."]
    :dancer [:h1 "dancer will be here."]
    :review [:h1 "review will be here."]
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
      (layout/frame
       {:nav {:selection :club}
        :session session}
       [:div.container-md.px-3.px-sm-4.px-xl-5
        [:div.row
         [:div.py-3.py-sm-4
          [:h1.h3.lh-base.mb-1 (get place :place/label)]
          (tabs selection handle)
          (content selection place address)]]]))))

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
   ["/review" {:get (page :review)}]
   ["/gallery" {:get (page :gallery)
                :post upload}]])
