(ns arcaneflare.database.place.root
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]
   [arcaneflare.database.place.social :as place.social]))

(defn upsert!
  [{:place/keys [id name handle address city
                 district state zipcode country
                 county region lat lon phone-number]}]
  (let [q {:insert-into :place
           :columns [:id :name :handle :address :city
                     :district :state :zipcode :country
                     :county :region :lat :lon :phone-number]
           :values [[id name handle address city
                     district state zipcode country
                     county region lat lon phone-number]]
           :on-conflict [:id]
           :do-update-set {:name :excluded.name
                           :handle :excluded.handle
                           :address :excluded.address
                           :city :excluded.city
                           :district :excluded.district
                           :state :excluded.state
                           :zipcode :excluded.zipcode
                           :country :excluded.country
                           :county :excluded.county
                           :region :excluded.region
                           :lat :excluded.lat
                           :lon :excluded.lon
                           :phone-number :excluded.phone_number}}]
    (db.base/exc (honey.sql/format q))))

(defn single-by
  [{:keys [place/id place/handle
           place/socials?]}]
  (let [where (cond id [:= :id id]
                    handle [:= :handle handle])
        q {:select [:*] :from :place :where where}
        {:keys [place/id] :as root} (first (db.base/run q))
        read (cond-> root
               (and root socials?)
               (assoc :place/socials
                      (place.social/map-multi-by
                       {:place/id id})))]
    (when-not read
      (throw (ex-info "place not found"
                      {:errorr :not-found})))
    read))

(defn full-list [_]
  (let [q {:select [:id :handle :name]
           :from [:place]
           :order-by [:name]}]
    (db.base/exc (sql/format q))))
