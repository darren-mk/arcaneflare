(ns tia.page.home
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "home page"]])