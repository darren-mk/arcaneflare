(ns arcaneflare.state
  (:require
   [reagent.core :as r]))

(defonce match
  (r/atom nil))

(defonce theme
  (r/atom :light))

(defonce geographies
  (r/atom {}))

(defonce token
  (r/atom nil))

(defonce member
  (r/atom nil))