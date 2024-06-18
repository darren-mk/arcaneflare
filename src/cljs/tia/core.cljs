(ns tia.core
  (:require
   [reagent.dom :as rgd]))

(defn ^:dev/after-load start []
  (js/console.log "start!"))

(defn ^:dev/before-load stop []
  (js/console.log "stop!"))

(defn ^:export init []
  (rgd/render
   [:div [:p "abc!"]]
   (.getElementById js/document "tia")))

(comment 
  (start)
  (stop))