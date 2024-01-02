(ns tia.pages.clubs.root
  (:require
   [tia.data :as data]
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   (vec (cons
         :div
         (mapv (fn [country]
                 [:a {:href (str "/clubs/country/"
                                 (-> country key name))}
                  (-> country val :full-name)])
               data/countries)))))

(def routes
  ["/clubs" {:get page}])
