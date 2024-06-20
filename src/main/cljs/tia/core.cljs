(ns tia.core
  (:require
   [reagent.dom.client :as rdc]))

(defonce root-container
  (rdc/create-root 
   (js/document.getElementById "tia")))

(defn ^:dev/after-load start []
  (js/console.log "start!"))

(defn ^:dev/before-load stop []
  (js/console.log "stop!"))

(defn ^:export init []
  (rdc/render
   root-container
   [:div [:p "abc!"]]))

(comment 
  (start)
  (stop))