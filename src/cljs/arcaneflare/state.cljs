(ns arcaneflare.state
  (:require
   [reagent.core :as r]))

(defonce theme
  (r/atom :dark))

(defn get-theme-tag []
  (.getElementById
   js/document "html"))

(defn set-theme [k]
  (.setAttribute
   (get-theme-tag)
   "data-theme"
   (name k)))

(defn coerce-theme []
  (set-theme @theme))

(defn toggle-theme []
  (let [new-theme (case @theme
                     :light :dark
                     :dark :light)]
    (reset! theme new-theme)
    (set-theme new-theme)))

(defonce token
  (r/atom nil))

(defonce location
  (r/atom #{}))
