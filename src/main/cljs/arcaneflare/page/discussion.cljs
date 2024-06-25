(ns arcaneflare.page.discussion
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "discussion page"]])