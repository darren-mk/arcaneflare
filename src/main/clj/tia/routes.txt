(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.admin :as admin]
   [tia.pages.place.core :as place]
   [tia.pages.places :as places] 
   [tia.pages.landing :as landing]
   [tia.pages.login :as login]
   [tia.pages.logout :as logout]
   [tia.pages.region :as region]
   [tia.pages.reviews :as reviews]
   [tia.pages.signup :as signup] 
   [tia.pages.post :as post]
   [tia.middleware :as mw]
   [ring.util.response]))

(defn routes []
  ["" {:middleware [#_middleware/wrap-csrf
                    mw/wrap-formats
                    mw/sessionize]}
   ["/css" {:get style/core}]
   landing/routes
   places/routes
   place/routes
   signup/routes
   login/routes
   logout/routes
   #_database/routes
   region/routes
   reviews/routes
   #_storage/routes
   post/routes
   admin/routes])
