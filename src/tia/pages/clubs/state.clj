(ns tia.pages.clubs.state
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "clubs by country and state will be here"]))

(def routes
  ["/clubs/:country/:state" {:get page}])
