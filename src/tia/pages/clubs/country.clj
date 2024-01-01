(ns tia.pages.clubs.country
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "clubs by country will be here"]))

(def routes
  ["/clubs/:country" {:get page}])
