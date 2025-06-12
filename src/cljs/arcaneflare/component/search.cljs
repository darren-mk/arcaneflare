(ns arcaneflare.component.search
  (:require
   [reagent.core :as r]
   ))

#_(when-not @countries
    (http/tunnel
     [:api.public.geo/countries {}]
     #(reset! countries %)
     #(js/alert %)))

(defn nation [v]
  [:div.column.is-one-quarter
   [:div.field
    [:label.label "Country"]
    [:div.control
     [:div.select
      [:select {:value (or (-> v deref :country) "")
                :on-change #(do (swap! v assoc :country
                                       (-> % .-target .-value))
                                (swap! v dissoc :state))}
       [:option {:value ""} "Select Country"]
       (for [{:keys [id name]} (get v :countries)]
         ^{:key id} [:option {:value id} name])]]]]])

(defn fraction [v]
  [:div.field.has-addons.mb-4
   [:div.control
    [:input.input
     {:type "text"
      :placeholder "Search by name!@@@"
      :value (-> v deref :fraction)
      :on-change #(swap! v assoc :fraction
                         (-> % .-target .-value))}]]])

(defn root []
  (let [v (r/atom {:abc 123
                   :countries [{:id 1 :name "USA"}]})]
    (fn []
      [:div
       [:p (str "@@@@" @v)]
       [nation v]
       [fraction v]])))
