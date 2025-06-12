(ns arcaneflare.components.geographical
  (:require
   [reagent.core :as r]
   [arcaneflare.http :as http]
   [arcaneflare.state :as state]))

(defonce fragment
  (r/atom nil))

(defonce items
  (r/atom nil))

(defonce search-mode?
  (r/atom false))

(defn clean []
  (reset! fragment nil)
  (reset! items nil))

(defn pull [fraction]
  (http/tunnel
   [[:api.public.geo/multi-by
     {:geo/fraction fraction}]
    [:api.public.place/multi-by-fraction
     {:place/fraction fraction}]]
   #(reset! items %)
   #(js/alert "?")))

(defn tags [{:keys [reaction]}]
  [:div.is-flex.is-flex-direction-row
   {:style {:gap 10}}
   [:button.icon.is-small
    {:on-click #(do (swap! search-mode? not)
                    (reset! items nil)
                    (when reaction (reaction)))}
    [:i.fa-solid.fa-magnifying-glass]]
   (for [[id {geo-name :geo/full-name
              place-name :place/name}] @state/geography]
     ^{:key id}
     [:span.tag.is-link.is-light
      (or geo-name place-name)
      [:button.icon.ml-1
       {:on-click #(do (swap! state/geography dissoc id)
                       (when reaction (reaction)))}
       [:i.fa-solid.fa-xmark {:aria-hidden true}]]])])

(defn bar [{:keys [reaction]}]
  (when @search-mode?
    [:div.container.is-flex.is-flex-direction-column
     {:style {:gap 20}}
     [:input.input
      {:placeholder "Search..."
       :on-change (fn [e]
                    (reset! fragment (-> e .-target .-value str))
                    (if (seq @fragment)
                      (pull @fragment)
                      (reset! items nil)))}]
     (when (seq @fragment)
       [:div.container.is-flex.is-flex-direction-column
        {:style {:gap 20}}
        (for [{geo-id :geo/id
               geo-name :geo/full-name
               geo-kind :geo/kind
               place-id :place/id
               place-name :place/name
               :as m} @items]
          ^{:key (or geo-id place-id)}
          [:button
           {:on-click #(do (swap! state/geography
                                  assoc (or geo-id place-id) m)
                           (reset! search-mode? false)
                           (when reaction (reaction)))}
           (str (or geo-name place-name) " ("
                (or geo-kind "club") ")")])])]))
