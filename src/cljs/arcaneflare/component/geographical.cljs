(ns arcaneflare.component.geographical
  (:require
   [arcaneflare.state :as state]))

(defn tags [& [{:keys [reaction]}]]
  [:div.is-flex.is-flex-direction-row {:style {:gap 10}}
   (for [[id {geo-name :geo/full-name
              place-name :place/name}] @state/geographies]
     [:span.tag.is-link.is-light
      (or geo-name place-name)
      [:button.icon.ml-1
       {:on-click #(do (swap! state/geographies dissoc id)
                       (when reaction (reaction)))}
       [:i.fa-solid.fa-xmark {:aria-hidden true}]]])
   [:span.tag.is-link.is-light
    [:a.icon.is-small {:href "/#/geography"}
     [:i.fa-solid.fa-plus {:aria-hidden true}]]]])