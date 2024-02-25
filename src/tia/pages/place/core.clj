(ns tia.pages.place.core
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.calc :refer [>s] :as c]
   [tia.components.inputs :as comp-input]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.middleware :as mw]
   [tia.pages.place.common :as p-common]
   [tia.pages.place.review :as p-review]
   [tia.storage :as storage]
   [tia.util :as u]))

(def uri
  "/place")

(def selections-enum
  (into [:enum] p-common/selections))

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

(defn content
  [selection place address]
  (case selection
    :info (info place address)
    :event [:h1 "event will be here."]
    :menu [:h1 "menu will be here."]
    :dancer [:h1 "dancer will be here."]
    :gallery (gallery place)))

(defn tab [ident selection place]
  (let [handle (:place/handle place)
        href (c/path :place handle ident)
        label (-> ident name cstr/capitalize)
        selected? (= ident selection)]
    [:li.nav-item
     [:a.nav-link {:href href
                   :class (if selected? "active" nil)
                   :aria-current selected?}
      label]]))

(defn tabs [selection place]
  (let [f #(tab % selection place)
        elems (mapv f p-common/selections)]
    (into [:ul.nav.nav-tabs]
          elems)))

(m/=> page
      [:=> [:cat selections-enum]
       :any])

(defn page [selection]
  (fn [{:keys [session place address] :as _req}]
    (l/page
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
  [(str uri "/:handle")
   {:middleware [mw/handle->place+address]}
   ["/info" {:get (page :info)}]
   ["/event" {:get (page :event)}]
   ["/menu" {:get (page :menu)}]
   ["/dancer" {:get (page :dancer)}]
   p-review/routes
   ["/gallery" {:get (page :gallery)
                :post upload}]])
