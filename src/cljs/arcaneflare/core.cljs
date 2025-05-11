(ns arcaneflare.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.section.header :as header]
   [arcaneflare.pages.home :as home-pg]
   [arcaneflare.pages.account :as account-pg]
   [arcaneflare.pages.login :as login-pg]
   [arcaneflare.pages.signup :as signup-pg]
   [arcaneflare.pages.location :as location-pg]
   [arcaneflare.state :as state]))

(defonce root-container
  (rdc/create-root
   (.getElementById
    js/document
    "arcaneflare")))

(defonce match
  (r/atom nil))

(def routes
  [["/" {:name :page/landing :view home-pg/node}]
   ["/account" {:name :page/account :view account-pg/node}]
   ["/login" {:name :page/login :view login-pg/node}]
   ["/signup" {:name :page/signup :view signup-pg/node}]
   ["/location" {:name :page/location :view location-pg/node}]])

(defn current-page []
  [:div.container.is-fullhd
   [header/navbar]
   (when @match
     (let [view (-> @match :data :view)]
       [view @match]))])

(defn ^:dev/after-load start []
  (rtfe/start!
   (rtf/router routes)
   (fn [m]
     (state/coerce-theme)
     (reset! match m))
   {:use-fragment true})
  (rdc/render
   root-container
   [current-page])
  (println "arcaneflare frontend app started."))

(defn ^:dev/before-load stop []
  (println "arcaneflare frontend app stopped."))

(defn ^:export init []
  (start))

(comment
  (start)
  (stop))
