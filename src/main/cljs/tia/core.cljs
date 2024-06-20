(ns tia.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]))

(defonce root-container
  (rdc/create-root
   (.getElementById js/document "tia")))

(defonce match
  (r/atom nil))

(defn landing-page []
  [:div 
   [:p "landing!"]
   [:a {:href "/#/about"}
    "to about page"]])

(defn about-page []
  [:div [:p "about!"]
   [:a {:href "/#/"}
    "to landing page"]])

(def routes
  [["/"
    {:name ::landing-page
     :view landing-page}]
   ["/about"
    {:name ::about-page
     :view about-page}]])

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