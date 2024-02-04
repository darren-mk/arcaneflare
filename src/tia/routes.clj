(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.place :as place]
   [tia.pages.places :as places]
   [tia.pages.database :as database]
   [tia.pages.landing :as landing]
   [tia.pages.login :as login]
   [tia.pages.region :as region]
   [tia.pages.reviewlist :as reviewlist]
   [tia.pages.signup :as signup]
   [tia.pages.storage :as storage]
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
   #_#_#_#_#_#_  login/routes
   database/routes
   region/routes
   reviewlist/routes
   signup/routes
   storage/routes])
