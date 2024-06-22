(ns tia.page.login
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div 
   [nav/node]
   [:p "login page"]])