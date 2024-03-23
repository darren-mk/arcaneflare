(ns tia.db.place
  (:require
   [malli.core :as m]
   [tia.calc :as c]
   [tia.db.common :as dbc]
   [tia.db.address :as db-address]
   [tia.model :as model]
   [tia.util :as u]))

(defn coerce [m]
  (-> m c/kebab-m
      (u/update-if-exists :misc dbc/->edn)
      (u/update-if-exists :sector keyword)
      (u/update-if-exists :handle keyword)
      (u/update-if-exists :nudity keyword)
      (u/update-if-exists :status keyword)
      (u/map->nsmap :place)))

(u/mf coerce [:map model/place])

(defn get-all []
  (let [qr {:select [:*]
            :from [:place]}]
    (map coerce (dbc/hq qr))))

(comment
  (take 1 (get-all))
  :=> '(#:place{:misc
                {:phone "1-973-684-7678",
                 :website "http://johnnyashitchingpost.com/",
                 :facebook "https://www.facebook.com/JohnnyAsHitchingPost",
                 :twitterx "https://twitter.com/hitchingpostnj",
                 :instagram "https://www.instagram.com/Hitching_Post_/"},
                :address-id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
                :sector :strip-club,
                :edited-at #inst "2024-03-23T16:07:26.976000000-00:00",
                :status :operational,
                :id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4",
                :created-at #inst "2024-03-23T16:07:26.976000000-00:00",
                :nudity :none,
                :label "Johnny A’s Hitching Post",
                :handle :johnny-as-hitching-post}))

(defn create!
  [{:place/keys [id sector label handle nudity
                 status address-id misc created-at edited-at]
    :as place}]
  (assert (m/validate model/place place))
  (dbc/hd {:insert-into [:place]
           :columns [:id :sector :label :handle :nudity :status
                     :address_id :misc :created_at :edited_at]
           :values [[id (name sector) label (name handle) (name nudity)
                     (name status) address-id (dbc/->jsonb misc)
                     created-at edited-at]]}))

(comment
  (create!
   #:place{:id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
           :sector :strip-club
           :label "Johnny A’s Hitching Post"
           :handle :johnny-as-hitching-post
           :nudity :none
           :status :operational
           :address-id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
           :misc {:phone "1-973-684-7678"
                  :facebook "https://www.facebook.com/JohnnyAsHitchingPost"
                  :website "http://johnnyashitchingpost.com/"
                  :twitterx "https://twitter.com/hitchingpostnj"
                  :instagram "https://www.instagram.com/Hitching_Post_/"}
           :created-at (u/now)
           :edited-at (u/now)}))

(defn find-places-in-city [city]
  (let [qr {:select [:place.*]
            :from [:place]
            :join [:address [:= :place.address-id :address.id]]
            :where [[:= :address.city city]]}]
    (map coerce (dbc/hq qr))))

(comment
  (find-places-in-city "Paterson")
  :=> '(#:place{:misc
                {:phone "1-973-684-7678",
                 :website "http://johnnyashitchingpost.com/",
                 :facebook "https://www.facebook.com/JohnnyAsHitchingPost",
                 :twitterx "https://twitter.com/hitchingpostnj",
                 :instagram "https://www.instagram.com/Hitching_Post_/"},
                :address-id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
                :sector :strip-club,
                :edited-at #inst "2024-03-23T16:07:26.976000000-00:00",
                :status :operational,
                :id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4",
                :created-at #inst "2024-03-23T16:07:26.976000000-00:00",
                :nudity :none,
                :label "Johnny A’s Hitching Post",
                :handle :johnny-as-hitching-post}))

(defn find-places-by-state [state]
  (let [qr {:select [:place.*]
            :from [:place]
            :join [:address [:= :place.address_id :address.id]]
            :where [[:= :address.state state]]}]
    (map coerce (dbc/hq qr))))

(comment
  (find-places-by-state "NJ"))

(defn find-place-by-handle [handle]
  (let [qr {:select [:place.*]
            :from [:place]
            :where [[:= :place.handle (name handle)]]}]
    (-> qr dbc/hq first coerce)))

(comment
  (find-place-by-handle :johnny-as-hitching-post))

(defn find-place-and-address [handle]
  (let [place (find-place-by-handle handle)
        address (db-address/find-address-by-handle handle)]
    {:place place
     :address address}))

(comment
  (find-place-and-address :johnny-as-hitching-post))

(defn place-handle->id [handle]
  (let [qr {:select [:place.id]
            :from [:place]
            :where [[:= :place.handle (name handle)]]}]
    (-> qr dbc/hq first :id)))

(comment
  (place-handle->id :johnny-as-hitching-post)
  :=> #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4")

(defn place-id->handle [id]
  (let [qr {:select [:place.handle]
            :from [:place]
            :where [[:= :place.id id]]}]
    (-> qr dbc/hq first :handle keyword)))

(comment
  (place-id->handle #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4")
  :=> :johnny-as-hitching-post)
