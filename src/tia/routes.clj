(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.clubs.root :as clubs-root]
   [tia.pages.clubs.country :as clubs-country]
   [tia.pages.clubs.state :as clubs-state]
   [tia.pages.clubs.individual :as clubs-individual]
   [tia.pages.database :as database]
   [tia.pages.landing :as landing]
   [tia.pages.storage :as storage]
   [tia.middleware :as middleware]
   [ring.util.response]))

(defn routes []
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/css" {:get style/core}]
   clubs-root/routes
   clubs-country/routes
   clubs-state/routes
   clubs-individual/routes
   landing/routes
   database/routes
   storage/routes])
