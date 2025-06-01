(ns arcaneflare.database.place.seed
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [arcaneflare.database.place.root :as place.root]
   [arcaneflare.database.place.social :as place.social]))

(defn bring [csv-path]
  (with-open [rdr (io/reader csv-path)]
    (let [[headers & rows] (csv/read-csv rdr)
          keys (map keyword headers)]
      (->> rows
           (map #(zipmap keys %))
           vec))))

(defn populate! []
  (doseq [{:keys [id name handle address city
                  district state zipcode country
                  county region lat lon phone-number
                  website twitter instagram facebook]}
          (bring "resources/seeds/places.csv")]
    (let [id' (parse-uuid id)
          lat' (Float/parseFloat lat)
          lon' (Float/parseFloat lon)]
      (place.root/upsert!
       #:place{:id id' :name name :handle handle
               :address address :city city
               :district district :state state
               :zipcode zipcode :country country
               :county county :region region
               :lat lat' :lon lon'
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

(comment
  (populate!))
