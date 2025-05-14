(ns arcaneflare.state
  (:require
   [reagent.core :as r]))

(defonce theme
  (r/atom nil))

(defonce token
  (r/atom nil))

(defonce areas-chosen
  (r/atom #{}))

(defonce clubs-loaded
  (r/atom
   [{:name "Red Velvet"
     :rating 4.3
     :tags ["VIP" "Friendly Staff"]
     :desc "Upscale club with regular shows"}
    {:name "Neon Nights"
     :rating 3.9
     :tags ["Budget" "Late Night"]
     :desc "Open late and great drink deals"}]))

(defonce account-synced
  (r/atom
   #:user{:name "dukovmanutei123"
          :email "dukov123@eml.com"
          :description "Bio: I like checking out nightlife spots and writing honest reviews."
          :areas []
          :bookmarks []
          :threads []
          :replies []}))
