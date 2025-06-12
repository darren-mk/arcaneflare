(ns arcaneflare.database.place.root
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as base]
   [arcaneflare.database.place.social :as place.social]))

(defn upsert!
  [{:place/keys [id name handle address
                 geo-id lat lon phone-number]}]
  (let [q {:insert-into :place
           :columns [:id :name :handle :address :geo-id
                     :lat :lon :phone-number]
           :values [[id name handle address
                     geo-id lat lon phone-number]]
           :on-conflict [:id]
           :do-update-set {:name :excluded.name
                           :handle :excluded.handle
                           :address :excluded.address
                           :geo-id :excluded.geo-id
                           :lat :excluded.lat
                           :lon :excluded.lon
                           :phone-number :excluded.phone_number}}]
    (base/exc (honey.sql/format q))))

(defn seed! []
  (doseq [{:keys [id name handle address geo-id
                  lat lon phone-number website
                  twitter instagram facebook]}
          (base/bring-csv "resources/seeds/places.csv")]
    (let [id' (parse-uuid id)
          geo-id' (parse-uuid geo-id)
          lat' (Float/parseFloat lat)
          lon' (Float/parseFloat lon)
          update-social! (fn [id platform website]
                           (place.social/upsert!
                            {:place/id id
                             :place.social/platform platform
                             :place.social/url website}))]
      (upsert!
       #:place{:id id' :name name :handle handle
               :address address :geo-id geo-id'
               :lat lat' :lon lon'
               :phone-number phone-number})
      (when (seq website)
        (update-social! id' :website website))
      (when (seq twitter)
        (update-social! id' :twitter twitter))
      (when (seq instagram)
        (update-social! id' :instagram instagram))
      (when (seq facebook)
        (update-social! id' :facebook facebook)))))

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

(defn full-list [_]
  (let [q {:select [:id :handle :name]
           :from [:place]
           :order-by [:name]}]
    (base/run q)))

(defn multi-by-geo-ids
  [{geo-ids :geo/ids
    page :place.result/page
    per :place.result/per}]
  (let [page' (or page 1)
        per' (or per 30)
        q {:with-recursive
           [[:geo-subtree
             {:union-all [{:select [:id] :from [:geo]
                           :where [:in :id geo-ids]}
                          {:select [:g.id] :from [[:geo :g]]
                           :join [[:geo-subtree :s]
                                  [:= :g.parent-id :s.id]]}]}]]
           :select [:p.*]
           :from [[:place :p]]
           :join [[:geo-subtree :s] [:= :p.geo-id :s.id]]
           :limit per'
           :offset (* per' (dec page'))}]
    (base/run q)))

(defn multi-by-ids
  [{ids :place/ids
    page :place.result/page
    per :place.result/per}]
  (let [page' (or page 1)
        per' (or per 30)
        q (merge {:select [:*]
                  :from :place
                  :where [:in :id ids]
                  :limit per'
                  :offset (* per' (dec page'))})]
    (base/run q)))

(defn multi-by-fraction
  [{fraction :place/fraction
    page :place.result/page
    per :place.result/per}]
  (let [wrap #(str "%" % "%")
        page' (or page 1)
        per' (or per 30)
        q (merge {:select [:*]
                  :from :place
                  :where [:ilike :name (wrap fraction)]
                  :limit per'
                  :offset (* per' (dec page'))})]
    (base/run q)))
