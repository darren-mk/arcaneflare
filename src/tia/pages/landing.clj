(ns tia.pages.landing
  (:require
   [tia.style :as stl]
   [tia.layout :as layout]))

(defn ttt [_req]
  (layout/html
   [:h1 {:class (stl/c :css/nice)}
    "hello"]))
