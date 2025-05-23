(ns arcaneflare.database.place.root
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hugsql.core :as hugsql]
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

(hugsql/def-db-fns "sql/place.sql")
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

(defn single-by
  [{:keys [place/id place/handle]}]
  (let [where (cond id [:= :id id]
                    handle [:= :handle handle])
        q {:select [:*]
           :from :place
           :where where}]
    (first (db.base/exc (sql/format q)))))

(defn full-list [_]
  (let [q {:select [:id :handle :name]
           :from [:place]
           :order-by [:name]}]
    (db.base/exc (sql/format q))))

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
