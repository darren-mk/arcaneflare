(ns tia.feed
  (:require
   [clojure.string :as cstr]
   [clj-http.client :as client]
   [cheshire.core :as json]
   [malli.core :as m]
   [mount.core :refer [defstate]]
   [tia.calc :as calc]
   [tia.config :as config]
   [tia.data :as data]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(declare gplace-api-key)

(defstate ^:dynamic gplace-api-key
  :start (:gmap-api-key config/env)
  :stop nil)

(defn request [text-query]
  (let [mask (calc/mask-gplace data/gplace-fields)
        payload {:save-request? true
           :headers {"Content-Type" "application/json"
                     "X-Goog-FieldMask" mask
                     "X-Goog-Api-Key" gplace-api-key}
           :form-params {:textQuery text-query}}]
    (-> (client/post data/gplace-uri payload) :body
        (json/parse-string keyword) :places)))

(defn transform-gplace
  [industry
   {:keys [googleMapsUri internationalPhoneNumber
           websiteUri displayName businessStatus
           id formattedAddress]}]
  (let [address-id (u/uuid)
        address (calc/parse-address address-id formattedAddress)
        place-id (u/uuid)
        label (:text displayName)
        status (-> businessStatus cstr/lower-case keyword)
        place (merge {:place/id place-id
                      :place/sector industry
                      :place/language :en
                      :place/label label
                      :place/handle (calc/handlify address label)
                      :place/status status
                      :place/address-id address-id
                      :place/google-id id
                      :place/google-uri googleMapsUri}
                     (when websiteUri {:place/website websiteUri})
                     (when internationalPhoneNumber
                       {:place/phone internationalPhoneNumber}))]
    [place address]))

(def transit
  (atom nil))

(defn store
  "record places by state into db"
  [industry text-query]
  (let [google-data (request text-query)
        transform-f (partial transform-gplace industry)
        tia-data (->> google-data
                      (map transform-f)
                      (apply concat) vec)]
    (reset! transit tia-data)))

(comment
  (store :strip-club
         "strip clubs in passaic county, new jersey")
  (count @transit)
  (count (filter #(:place/handle %) @transit))
  (count (filter #(:address/street %) @transit))
  (->> (filter #(:place/handle %) @transit)
       (map #(m/validate model/place %))
       (every? true?))
  (->> (filter #(:address/street %) @transit)
       (map #(m/validate model/address %))
       (every? true?))
  (->> @transit
       (mapv :place/handle)
       (remove nil?) vec)
  (->> @transit
       (mapv :address/city)
       (remove nil?) vec)
  (->> @transit
       (mapv :address/state)
       (remove nil?) vec)
  (doseq [m @transit]
    (dbc/upsert! m))
  (dbc/count-all-having-key
   :address/id)
  (dbc/count-all-having-key
   :place/id))
