(ns tia.pages.place.gallery
  (:require
   [tia.calc :as c]
   [tia.db.place :as db-place]
   [tia.db.file :as db-file]
   [tia.db.common :as db-common]
   [tia.pages.place.common :as place-common]
   [tia.storage :as storage]))

(defn uri [handle elems]
  (-> [c/path :place handle :gallery elems]
      flatten eval))

(def make-page
  (partial place-common/paginate :gallery))

(defn card [{:file/keys [post-id object-key]}]
  (let [presigned-url (storage/presign-url object-key)
        {:post/keys [place-id title]} (db-common/pull-by-id post-id)
        handle (db-place/place-id->handle place-id)]
    [:div {:class "card" :style "width: 18rem;"}
     [:img {:src presigned-url}]
     [:div {:class "card-body"}
      [:span {:class "badge text-bg-success"}
       "review"]
      [:a {:href (c/path :place handle :reviews post-id :read)
           :class "card-text"}
       title]]]))

(defn gallery-section [{:keys [place]}]
  (let [{:place/keys [id]} place
        medias (db-file/get-files-by-place-id id)]
    [:div
     [:div {:style {:display :flex :flex-direction :row :gap :30px}}
      (for [media medias]
        (card media))]]))

(def routes
  ["/gallery"
   [["" {:get (make-page gallery-section)}]]])
