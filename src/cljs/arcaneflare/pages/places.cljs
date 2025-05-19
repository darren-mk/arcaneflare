(ns arcaneflare.pages.places
  (:require
   [arcaneflare.state.loaded :as loaded-state]))

(defn card [place]
  [:div.column.is-full-mobile.is-half-tablet.is-one-third-desktop
   {:key (:name place)}
   [:div.card
    [:div.card-content
     [:p.title.is-5 (:name place)]
     [:p.subtitle.is-6 (:desc place)]
     [:div.tags.mb-2
      (for [tag (:tags place)]
        [:span.tag.is-info tag])]
     [:p.has-text-weight-bold.mt-2
      (str "⭐ " (:rating place))]]]])

(defn node [{:keys [query-params]}]
  (let [page (get query-params :page 1)
        places @loaded-state/places]
    [:section.section
     [:h1 "page: " page]
     [:div.container
      ;; === Filter Bar ===
      [:div.columns.is-multiline.mb-5
       [:div.column.is-one-third
        [:div.select.is-fullwidth
         [:select
          [:option "All Cities"]
          [:option "New York"]
          [:option "Las Vegas"]]]]
       [:div.column.is-one-third
        [:div.control
         [:input.input {:type "text"
                        :placeholder "Search tags..."}]]]
       [:div.column.is-one-third
        [:div.select.is-fullwidth
         [:select
          [:option "Sort by Rating"]
          [:option "Sort by Name"]
          [:option "Sort by Distance"]]]]]
      [:div.columns.is-multiline
       (for [p places]
         [card p])]]]))