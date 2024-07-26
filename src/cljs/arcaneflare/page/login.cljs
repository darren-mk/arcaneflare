(ns arcaneflare.page.login
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div 
   [nav/node]
   [:p "login page"]])