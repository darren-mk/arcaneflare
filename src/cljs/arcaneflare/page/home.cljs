(ns arcaneflare.page.home
  (:require
   [reagent-mui.material.button :refer [button]]))

(defn node []
  [:div
   [button {:variant :outlined
            :color :secondary}
    "yo, mui!"]
   [:p "home page"]])
