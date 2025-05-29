(ns arcaneflare.database.place.root
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

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
           :where where}
        read (first (db.base/run q))]
    (when-not read
      (throw (ex-info "place not found"
                      {:errorr :not-found})))
    read))

(defn full-list [_]
  (let [q {:select [:id :handle :name]
           :from [:place]
           :order-by [:name]}]
    (db.base/exc (sql/format q))))

(comment
  (time
   (upsert-seeds!)))
