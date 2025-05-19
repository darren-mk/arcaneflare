(ns arcaneflare.state.loaded
  "states loaded from the backend.
   client asks to load and consumes but cannot mutate."
  (:require
   [reagent.core :as r]))

(defonce places
  (r/atom {}))