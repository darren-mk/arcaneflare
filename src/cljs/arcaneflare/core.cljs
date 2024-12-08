(ns arcaneflare.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reagent-mui.colors :as clr]
   [reagent-mui.material.css-baseline :as cb]
   [reagent-mui.styles :as styles]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.component.nav :as nav]
   [arcaneflare.page.home :as home-pg]))

(defonce root-container
  (rdc/create-root
   (.getElementById
    js/document
    "arcaneflare")))

(defonce match
  (r/atom nil))

(def routes
  [["/" {:name :page/landing
         :view home-pg/node}]])

(def custom-theme
  {:palette {:primary clr/yellow
             :secondary clr/green}})

(defn current-page [] 
  [:<>
   [cb/css-baseline]
   [styles/theme-provider
    (styles/create-theme custom-theme)
    [:div ;; some frame design
     [nav/node]
     (when @match
       (let [view (-> @match :data :view)]
         [view @match]))]]])

(defn ^:dev/after-load start []
  (rtfe/start!
   (rtf/router routes)
   (fn [m] (reset! match m))
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
