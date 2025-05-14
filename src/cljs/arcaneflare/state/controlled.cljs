(ns arcaneflare.state.controlled
  "loaded from backend initially
   but expects constantly controlled by client,
   and synced backend."
  (:require
   [reagent.core :as r]))

(defonce theme
  (r/atom nil))

(defonce areas
  (r/atom #{}))

(defonce user
  (r/atom
   #:user{:name "dukovmanutei123"
          :email "dukov123@eml.com"
          :description "Bio: I like checking out nightlife spots and writing honest reviews."
          :areas []
          :bookmarks []
          :threads []
          :replies []}))
