(ns tia.pages.landing
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn page [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "hello"]))

(def routes
  ["/" {:get page}])
