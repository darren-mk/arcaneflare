(ns arcaneflare.state
  (:require
   [reagent.core :as r]
   [arcaneflare.local :as local]))

(defonce theme
  (r/atom nil))

(defn get-local-theme []
  (-> :theme local/read keyword))

(defn get-theme []
  (or @theme (get-local-theme)))

(defn get-theme-tag []
  (.getElementById
   js/document "html"))

(defn set-theme-tag [k]
  (.setAttribute
   (get-theme-tag)
   "data-theme"
   (name k)))

(defn coerce-theme []
  (set-theme-tag (get-theme)))

(defn toggle-theme []
  (let [current (get-theme)
        new-theme (case current
                    :light :dark
                    :dark :light)]
    (local/save :theme (name new-theme))
    (reset! theme new-theme)
    (set-theme-tag new-theme)))

#_
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
