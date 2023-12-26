(ns tia.pages.landing
  (:require
   [tia.layout :as layout]))

(defn ttt [_req]
  (layout/html [:h1 "hello"]))
