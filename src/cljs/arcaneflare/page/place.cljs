(ns arcaneflare.page.place
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "place page"]])