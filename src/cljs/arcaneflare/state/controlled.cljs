(ns arcaneflare.state.controlled
  "loaded from backend initially
   but expects constantly controlled by client,
   and synced backend."
  (:require
   [reagent.core :as r]))

(defonce theme
  (r/atom :light))

(defonce areas
  (r/atom #{}))

(defonce token
  (r/atom nil))

(defonce member
  (r/atom nil))

(defonce errors
  (r/atom {}))