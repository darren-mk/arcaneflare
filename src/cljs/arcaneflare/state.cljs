(ns arcaneflare.state
  (:require
   [reagent.core :as r]))

(defonce theme
  (r/atom nil))

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
