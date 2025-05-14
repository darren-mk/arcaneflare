(ns arcaneflare.pages.area
  (:require
   [clojure.string :as cstr]
   [reagent.core :as r]))

(defonce selected-tab
  (r/atom :all))

(defn tab [k]
  [:a (merge
       {:on-click #(reset! selected-tab k)}
       (when (= k @selected-tab)
         {:class :is-active}))
   (-> k name cstr/capitalize)])

(defn item [{:keys [id kind label]}]
  [:label.panel-block
   [:input {:type "checkbox"
            #_#_:checked true}]
   [:button
   {:on-click #(reset! selected-tab
                       (case kind
                         :country :states
                         :state :counties
                         :county :cities
                         :city :districts
                         :district :spots))}
    label]])

(defn node []
  [:div.container
   [:nav.panel
    [:p.panel-heading "areas"]
    [:div.panel-block
     [:p.control.has-icons-left
      [:input.input {:type "text"
                     :placeholder "Search"}]
      [:span.icon.is-left
       [:i.fas.fa-search {:aria-hidden true}]]]]
    [:p.panel-tabs
     (for [tb [:all :states :counties
               :cities :districts :spots]]
       ^{:key tb} [tab tb])]
    (for [it [{:id 123 :kind :state :label "New York"}]]
      ^{:key it} [item it])
    [:div.panel-block
     [:button.button.is-link.is-outlined.is-fullwidth
      "Reset all filters"]]]])