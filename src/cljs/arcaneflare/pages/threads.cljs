(ns arcaneflare.pages.threads
  (:require
   [arcaneflare.sections.geographical :as g]))

(defn node [{:keys [query-params]}]
  (let [page (or (get query-params :page 1) 0)
        reviews [{:user "Alex"
                  :club "Red Velvet"
                  :rating 4.5
                  :comment "Great vibe, friendly staff."}
                 {:user "Jamie"
                  :club "Neon Nights"
                  :rating 3.0
                  :comment "Music was too loud but decent drinks."}]]

    [:section.section
     [g/tags]
     [g/bar]
     [:div.container
      [:h1.title "User Reviews" page]
      ;; === Table ===
      [:table.table.is-striped.is-fullwidth
       [:thead
        [:tr
         [:th "Reviewer"]
         [:th "Club"]
         [:th "Rating"]
         [:th "Comment"]]]
       [:tbody
        (for [{:keys [user club rating comment]} reviews]
          [:tr {:key comment}
           [:td user]
           [:td club]
           [:td (str "⭐ " rating)]
           [:td comment]])]]
      ;; === Pagination ===
      [:nav.pagination.is-centered
       {:role "navigation" :aria-label "pagination"}
       [:a.pagination-previous "Previous"]
       [:a.pagination-next "Next"]
       [:ul.pagination-list
        (for [i (range 23)]
          ^{:key i}
          [:li [:a.pagination-link
                (when (= 3 i)
                  {:class :is-current})
                (inc i)]])]]]]))
