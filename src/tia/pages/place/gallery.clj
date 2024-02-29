(ns tia.pages.place.gallery
  (:require
   [tia.calc :as c]
   [tia.data :as d]
   [tia.db.place :as db-place]
   [tia.db.post :as db-post]
   [tia.db.image :as db-image]
   [tia.db.commentary :as db-commentary]
   [tia.db.common :as db-common]
   [tia.layout :as l]
   [tia.pages.place.common :as place-common]
   [tia.storage :as storage]
   [tia.util :as u]))

(defn uri [handle elems]
  (-> [c/path :place handle :gallery elems]
      flatten eval))

(def make-page
  (partial place-common/paginate :gallery))

(defn gallery-section [{:keys [place]}]
  (let [{:place/keys [id]} place
        images (db-image/get-images-by-place-id id)
        presigned-urls (map #(storage/presign-url (:image/objk %)) images)]
    [:div
     [:div {:style {:display :flex :flex-direction :row :gap :30px}}
      (for [purl presigned-urls]
        [:div {:class "card" :style "width: 18rem;"}
         [:img {:src purl}]
         [:div {:class "card-body"}
          [:p {:class "card-text"}
           "Some quick example text to build on the card title and make up the bulk of the card&#39;s content."]]])]]))

(def routes
  ["/gallery"
   [["" {:get (make-page gallery-section)}]]])
