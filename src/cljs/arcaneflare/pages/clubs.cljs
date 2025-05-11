(ns arcaneflare.pages.clubs
  (:require
   [arcaneflare.state :as state]))

(defn club-card [club]
  [:div.column.is-full-mobile.is-half-tablet.is-one-third-desktop
   {:key (:name club)}
   [:div.card
    [:div.card-content
     [:p.title.is-5 (:name club)]
     [:p.subtitle.is-6 (:desc club)]
     [:div.tags.mb-2
      (for [tag (:tags club)]
        [:span.tag.is-info tag])]
     [:p.has-text-weight-bold.mt-2
      (str "⭐ " (:rating club))]]]])

(defn node [{:keys [query-params]}]
  (let [page (get query-params :page 1)
        clubs @state/clubs]
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
         [:input.input {:type "text" :placeholder "Search tags..."}]]]
       [:div.column.is-one-third
        [:div.select.is-fullwidth
         [:select
          [:option "Sort by Rating"]
          [:option "Sort by Name"]
          [:option "Sort by Distance"]]]]]
      [:div.columns.is-multiline
       (for [club clubs]
         [club-card club])]]]))