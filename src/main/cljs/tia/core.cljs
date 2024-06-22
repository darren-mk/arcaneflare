(ns tia.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [tia.page.home :as home-pg]
   [tia.page.login :as login-pg]
   [tia.page.signup :as signup-pg]
   [tia.page.place :as place-pg]
   [tia.page.review :as review-pg]
   [tia.page.discussion :as discussion-pg]
   [tia.page.preference :as preference-pg]))

(defonce root-container
  (rdc/create-root
   (.getElementById js/document "tia")))

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