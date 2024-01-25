(ns tia.routes
  (:require
   [tia.style :as style]
   [tia.pages.club :as club]
   [tia.pages.clublist :as clublist]
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
   club/routes
   clublist/routes
   landing/routes
   login/routes
   database/routes
   region/routes
   reviewlist/routes
   signup/routes
   storage/routes])
