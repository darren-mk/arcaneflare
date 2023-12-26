(ns tia.pages.landing
  (:require
   [tia.layout :as layout]))

(defn ttt [_req]
  (layout/html (str "<p>"
                    _req
                    "</p>")))
