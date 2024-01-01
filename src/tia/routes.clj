(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.clubs.root :as clubs-root]
   [tia.pages.clubs.country :as clubs-country]
   [tia.pages.clubs.state :as clubs-state]
   [tia.pages.clubs.club :as clubs-club]
   [tia.pages.database :as database]
   [tia.pages.landing :as landing]
   [tia.middleware :as middleware]
   [ring.util.response]))

(defn routes []
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/css" {:get style/core}]
   clubs-root/routes
   clubs-country/routes
   clubs-state/routes
   clubs-club/routes
   landing/routes
   database/routes])
