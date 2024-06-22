(ns tia.page.preference
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div 
   [nav/node]
   [:p "preference page"]])