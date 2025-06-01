(ns arcaneflare.state
  (:require
   [reagent.core :as r]))

(defonce match
  (r/atom nil))

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
