(ns arcaneflare.page.preference
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div 
   [nav/node]
   [:p "preference page"]])