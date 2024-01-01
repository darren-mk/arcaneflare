(ns tia.pages.clubs.club
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "a club will be here"]))

(def routes
  ["/clubs/:country/:state/:id" {:get page}])
