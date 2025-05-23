(ns arcaneflare.database.place
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hugsql.core :as hugsql]
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

(hugsql/def-db-fns "sql/place.sql")
(declare get-place-by-id)
(declare get-place-by-handle)
(declare get-full-list)
(declare love-place!)
(declare unlove-place!)
(declare get-place-loves)
(declare get-member-loved-places)
(declare vote-place!)
(declare remove-vote!)
(declare get-vote-score)
(declare get-member-vote)
(declare insert-thumbnail!)
(declare get-thumbnails-for-place)
(declare delete-thumbnail!)

(defn upsert!
  [{:place/keys [id name handle address city district state
                 zipcode country county region lat lon phone-number
                 website-url twitter-url instagram-url facebook-url]}]
  (let [q {:insert-into :place
           :columns [:id :name :handle :address :city :district :state
                     :zipcode :country :county :region :lat :lon :phone-number
                     :website-url :twitter-url :instagram-url :facebook-url]
           :values [[id name handle address city district state
                     zipcode country county region lat lon phone-number
                     website-url twitter-url instagram-url facebook-url]]
           :on-conflict [:id]
           :do-update-set {:name :excluded.name :handle :excluded.handle
                           :address :excluded.address :city :excluded.city
                           :district :excluded.district :state :excluded.state
                           :zipcode :excluded.zipcode :country :excluded.country
                           :county :excluded.county :region :excluded.region
                           :lat :excluded.lat :lon :excluded.lon
                           :phone-number :excluded.phone_number
                           :website-url :excluded.website_url
                           :twitter-url :excluded.twitter_url
                           :instagram-url :excluded.instagram_url
                           :facebook-url :excluded.facebook_url}}]
    (db.base/exc (honey.sql/format q))))

(defn load-seeds []
  (-> "seeds/places.edn"
      io/resource slurp
      edn/read-string))

(defn upsert-seeds! []
  (doseq [p (load-seeds)]
    (upsert! p)))

(defn single-by [{:keys [id handle]}]
  (cond id (get-place-by-id
            db.base/db {:id id})
        handle (get-place-by-handle
                db.base/db {:handle handle})))

(defn full-list []
  (into [] (get-full-list db.base/db

                          )))

(defn love!
  [{:keys [_member_id _place_id] :as m}]
  (love-place!
   db.base/db m))

(defn unlove!
  [{:keys [_member_id _place_id] :as m}]
  (unlove-place!
   db.base/db m))

(defn how-loved
  [{:keys [_place_id] :as m}]
  (:loves
   (get-place-loves
    db.base/db m)))

(defn loved-by-member
  [{:keys [_member_id] :as m}]
  (get-member-loved-places
   db.base/db m))

(defn vote!
  [{:keys [_member_id _place_id
           up-or-down] :as m}]
  (vote-place!
   db.base/db
   (assoc m :score
          (case up-or-down
            :up 1 :down -1))))

(defn unvote!
  [{:keys [_member_id _place_id] :as m}]
  (remove-vote!
   db.base/db m))

(defn vote-score
  [{:keys [_place_id member_id] :as m}]
  (let [f (if member_id
            get-member-vote
            get-vote-score)]
    (f db.base/db m)))

(defn add-thumbnail!
  [{:keys [_place_id _image_url _alt_text
           _caption _position] :as m}]
  (insert-thumbnail!
   db.base/db
   (assoc m :id (random-uuid))))

(defn get-thumbnails
  [{:keys [_place_id] :as m}]
  (get-thumbnails-for-place
   db.base/db m))

(defn remove-thumbnail!
  [{:keys [_id] :as m}]
  (delete-thumbnail!
   db.base/db m))

(comment
  (time
   (upsert-seeds!)))
