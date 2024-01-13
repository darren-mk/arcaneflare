(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.club :as club]
   [tia.pages.clublist :as clublist]
   [tia.pages.database :as database]
   [tia.pages.landing :as landing]
   [tia.pages.review :as review]
   [tia.pages.storage :as storage]
   [tia.middleware :as middleware]
   [ring.util.response]))

(defn routes []
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/css" {:get style/core}]
   club/routes
   clublist/routes
   landing/routes
   database/routes
   review/routes
   storage/routes])
