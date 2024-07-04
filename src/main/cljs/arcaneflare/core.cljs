(ns arcaneflare.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.page.home :as home-pg]
   [arcaneflare.page.login :as login-pg]
   [arcaneflare.page.signup :as signup-pg]
   [arcaneflare.page.place :as place-pg]
   [arcaneflare.page.review :as review-pg]
   [arcaneflare.page.discussion :as discussion-pg]
   [arcaneflare.page.preference :as preference-pg]))

(defonce root-container
  (rdc/create-root
   (.getElementById
    js/document
    "arcaneflare")))

(defonce match
  (r/atom nil))

(def routes
  [["/" {:name :page/landing
         :view home-pg/node}]
   ["/login" {:name :page/login
              :view login-pg/node}]
   ["/signup" {:name :page/signup
               :view signup-pg/node}]
   ["/place" {:name :page/place
              :view place-pg/node}]
   ["/review" {:name :page/review
               :view review-pg/node}]
   ["/discussion" {:name :page/discussion
                   :view discussion-pg/node}]
   ["/preference" {:name :page/preference
                   :view preference-pg/node}]])

(defn current-page [] 
  [:div
   (when @match
     (let [view (-> @match :data :view)]
       [view @match]))])

(defn ^:dev/after-load start []
  (println "start!"))

(defn ^:dev/before-load stop []
  (println "stop!"))

(defn ^:export init []
  (rtfe/start!
   (rtf/router routes)
   (fn [m] (reset! match m))
   {:use-fragment true})
  (rdc/render
   root-container
   [current-page]))

(comment 
  (start)
  (stop))
