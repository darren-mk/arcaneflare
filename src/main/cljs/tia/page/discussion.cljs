(ns tia.page.discussion
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "discussion page"]])