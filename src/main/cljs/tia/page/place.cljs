(ns tia.page.place
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "place page"]])