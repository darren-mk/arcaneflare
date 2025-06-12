(ns arcaneflare.pages.geography
  (:require
   [reagent.core :as r]
   [arcaneflare.component.geographical :as geographical]
   [arcaneflare.http :as http]
   [arcaneflare.state :as state]))

(defonce fragment
  (r/atom nil))

(defonce items
  (r/atom nil))

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

(defn node []
  [:div.container.is-flex.is-flex-direction-column
   {:style {:gap 20}}
   [geographical/tags]
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
        [:button
         {:on-click #(swap! state/geography
                            assoc (or geo-id place-id) m)}
         (str (or geo-name place-name) " ("
              (or geo-kind "club") ")")])])])
