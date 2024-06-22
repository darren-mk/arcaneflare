(ns tia.page.signup
  (:require
   [tia.component.nav :as nav]))

(defn node []
  [:div
   [nav/node]
   [:p "signup page"]])