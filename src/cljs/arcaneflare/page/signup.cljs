(ns arcaneflare.page.signup
  (:require
   [arcaneflare.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "signup page"]])