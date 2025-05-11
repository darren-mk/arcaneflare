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

(defonce clubs
  (r/atom
   [{:name "Red Velvet"
     :rating 4.3
     :tags ["VIP" "Friendly Staff"]
     :desc "Upscale club with regular shows"}
    {:name "Neon Nights"
     :rating 3.9
     :tags ["Budget" "Late Night"]
     :desc "Open late and great drink deals"}]))
