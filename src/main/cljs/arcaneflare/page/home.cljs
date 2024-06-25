(ns arcaneflare.page.home
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "home page"]])