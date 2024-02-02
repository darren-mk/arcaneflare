(ns tia.feed
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [clj-http.client :as client]
   [cheshire.core :as json]
   [malli.core :as m]
   [malli.error :as me]
   [mount.core :refer [defstate]]
   [tia.calc :as calc]
   [tia.config :as config]
   [tia.data :as data]
   [tia.model :as model]
   [tia.util :as u]))

(declare gplace-api-key)

(defstate ^:dynamic gplace-api-key
  :start (:gmap-api-key config/env)
  :stop nil)

(m/=> request
      [:=> [:cat model/industry
            model/country model/state]
       :any])

(defn request [industry country state]
  (let [elems [(-> data/industries industry :label) "in"
               (-> data/states state :label) ","
               (-> data/countries country :label)]
        tq (cstr/join " " elems)
        m {:save-request? true
           :headers {"Content-Type" "application/json"
                     "X-Goog-FieldMask" (calc/mask-gplace data/gplace-fields)
                     "X-Goog-Api-Key" gplace-api-key}
           :form-params {:textQuery tq}}]
    (-> (client/post data/gplace-uri m) :body
        (json/parse-string keyword) :places)))

(comment
  (request :club :us :nv))

(def address-gm->tia
  {:country "country"
   :zip "postal_code"
   :state "administrative_area_level_1"
   :county "administrative_area_level_2"
   :city "locality"
   :street "route"
   :number "street_number"})

(defn get-address-comp [gm-comps k]
  (let [target-type (get data/address-comp-types k)
        finding (->> gm-comps
                     (filter #(contains? (into #{} (:types %))
                                         (get address-gm->tia k)))
                     first :shortText)
        into-k? (= :keyword target-type)
        ->k (comp keyword cstr/lower-case)]
    (cond-> finding
      into-k? ->k)))

(defn transform-paymethods [gm-payoptions]
  (let [gmp gm-payoptions]
    (->> [(when (-> gmp :acceptsCashOnly) :cash)
          (when (-> gmp :acceptsDebitCards) :debit)
          (when (-> gmp :acceptsCreditCards) :credit)]
         (remove nil?)
         (into #{}))))

(defn transform-gplace
  [{:keys [googleMapsUri internationalPhoneNumber websiteUri
           paymentOptions displayName regularOpeningHours
           businessStatus id location addressComponents]}]
  (let [country (get-address-comp addressComponents :country)
        zip (get-address-comp addressComponents :zip)
        state (get-address-comp addressComponents :state)
        county (get-address-comp addressComponents :county)
        city (get-address-comp addressComponents :city)
        street (get-address-comp addressComponents :street)
        number (get-address-comp addressComponents :number)
        language :en
        address-id (u/uuid)
        address {:address/id address-id
                 :address/language language :address/googleid id
                 :address/googleuri googleMapsUri
                 :address/coordinate location :address/number number
                 :address/street street :address/city city
                 :address/county county :address/state state
                 :address/zip zip :address/country country}
        place-id (u/uuid)
        label (:text displayName)
        status (-> businessStatus cstr/lower-case keyword)
        paymethods (transform-paymethods paymentOptions)
        place {:place/id place-id :place/language language
               :place/industry :club :place/label label
               :place/handle (calc/handlify label) :place/status status
               :place/paymethods paymethods :place/website websiteUri
               :place/phone internationalPhoneNumber
               :place/schedules (:weekdayDescriptions regularOpeningHours)
               :place/address-id address-id}]
    [place address]))

(m/=> store
      [:=> [:cat model/industry model/country model/state]
       [:vector [:or model/address model/place]]])

(defn store
  "record places by state into db"
  [industry country state]
  (let [google-data (request industry country state)]
    (->> google-data
         (map transform-gplace)
         (apply concat)
         vec)))

(comment
  (store :club :us :ny))

(defn check [industry country state]
  (let [path [:data industry country
              (str (name state) ".edn")]
        loc (cstr/join "/" (mapv name path))
        data (-> loc
                 io/resource
                 slurp
                 edn/read-string)
        schema [:vector [:or model/address
                      model/place]]]
    (m/validate schema data)))

(comment
  (check :club :us :nv))

#_
(m/=> record
      [:=> [:cat model/industry model/country model/state]
       :any])
#_
(defn record
  "record places by state into db"
  [industry country state]
  (let [path [:data industry country
              (str (name state) ".edn")]
        loc (cstr/join "/" (mapv name path))
        data (-> loc
                 io/resource
                 slurp
                 edn/read-string)]))
