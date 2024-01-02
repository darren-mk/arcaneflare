(ns tia.pages.clubs.country
  (:require
   [tia.data :as data]
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [req]
  (let [country-key (-> req :path-params :country keyword)
        data (mapv (fn [state-key]
                     [:a {:href (str "/clubs/country/" (name country-key)
                                     "/state/" (name state-key))}
                      (-> data/states state-key :full-name)])
                   (:states (get data/countries country-key)))]
    (->> data (cons :div) vec layout/html)))

(def routes
  ["/clubs/country/:country" {:get page}])
