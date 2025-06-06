(ns arcaneflare.database.place.root
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as base]
   [arcaneflare.database.place.social :as place.social]))

(defn upsert!
  [{:place/keys [id name handle address city
                 district state zipcode nation
                 county lat lon phone-number]}]
  (let [q {:insert-into :place
           :columns [:id :name :handle :address :city
                     :district :state :zipcode :nation
                     :county :lat :lon :phone-number]
           :values [[id name handle address city
                     district state zipcode nation
                     county lat lon phone-number]]
           :on-conflict [:id]
           :do-update-set {:name :excluded.name
                           :handle :excluded.handle
                           :address :excluded.address
                           :city :excluded.city
                           :district :excluded.district
                           :state :excluded.state
                           :zipcode :excluded.zipcode
                           :nation :excluded.nation
                           :county :excluded.county
                           :lat :excluded.lat
                           :lon :excluded.lon
                           :phone-number :excluded.phone_number}}]
    (base/exc (honey.sql/format q))))

(defn seed! []
  (doseq [{:keys [id name handle address city
                  district state zipcode nation
                  county lat lon phone-number
                  website twitter instagram facebook]}
          (base/bring-csv "resources/seeds/places.csv")]
    (let [id' (parse-uuid id)
          lat' (Float/parseFloat lat)
          lon' (Float/parseFloat lon)]
      (upsert!
       #:place{:id id' :name name :handle handle
               :address address :city city
               :district district :state state
               :zipcode zipcode :nation nation
               :county county :lat lat' :lon lon'
               :phone-number phone-number})
      (when (seq website)
        (place.social/upsert!
         {:place/id id'
          :place.social/platform :website
          :place.social/url website}))
      (when (seq twitter)
        (place.social/upsert!
         {:place/id id'
          :place.social/platform :twitter
          :place.social/url twitter}))
      (when (seq instagram)
        (place.social/upsert!
         {:place/id id'
          :place.social/platform :instagram
          :place.social/url instagram}))
      (when (seq facebook)
        (place.social/upsert!
         {:place/id id'
          :place.social/platform :facebook
          :place.social/url facebook})))))

(defn single-by
  [{:keys [place/id place/handle
           place/socials?]}]
  (let [where (cond id [:= :id id]
                    handle [:= :handle handle])
        q {:select [:*] :from :place :where where}
        {:keys [place/id] :as root} (first (base/run q))
        read (cond-> root
               (and root socials?)
               (assoc :place/socials
                      (place.social/map-multi-by
                       {:place/id id})))]
    (when-not read
      (throw (ex-info "place not found"
                      {:errorr :not-found})))
    read))

(defn multi-by
  [{nation :place/nation state :place/state
    county :place/county city :place/city
    district :place/district
    fraction :place.search/fraction
    page :place.result/page per :place.result/per}]
  (let [wrap #(str "%" % "%")
        filters [(when nation [:ilike :nation (wrap nation)])
                 (when state [:ilike :state (wrap state)])
                 (when county [:ilike :county (wrap county)])
                 (when city [:ilike :city (wrap city)])
                 (when district [:ilike :district (wrap  district)])
                 (when fraction [:ilike :name (wrap fraction)])]
        page' (or page 1)
        per' (or per 30)
        where (into [:and] (remove nil? filters))
        q (merge {:select [:*]
                  :from :place
                  :limit per'
                  :offset (* per' (dec page'))}
                 (when (seq where) {:where where}))]
    (base/run q)))

(defn full-list [_]
  (let [q {:select [:id :handle :name]
           :from [:place]
           :order-by [:name]}]
    (base/run q)))

