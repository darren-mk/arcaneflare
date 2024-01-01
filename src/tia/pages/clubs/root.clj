(ns tia.pages.clubs.root
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "clubs will be here"]))

(def routes
  ["/clubs" {:get page}])
