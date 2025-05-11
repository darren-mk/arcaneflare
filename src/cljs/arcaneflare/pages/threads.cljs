(ns arcaneflare.pages.threads)

(defn node [{:keys [query-params]}]
  (let [page (get query-params :page 1)
        reviews [{:user "Alex"
                  :club "Red Velvet"
                  :rating 4.5
                  :comment "Great vibe, friendly staff."}
                 {:user "Jamie"
                  :club "Neon Nights"
                  :rating 3.0
                  :comment "Music was too loud but decent drinks."}]]

    [:section.section
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
      [:nav.pagination.is-centered {:role "navigation" :aria-label "pagination"}
       [:a.pagination-previous "Previous"]
       [:a.pagination-next "Next"]
       [:ul.pagination-list
        [:li [:a.pagination-link.is-current {:aria-label "Page 1"} "1"]]
        [:li [:a.pagination-link {:aria-label "Goto page 2"} "2"]]
        [:li [:a.pagination-link {:aria-label "Goto page 3"} "3"]]]]]]))