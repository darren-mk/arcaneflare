(ns arcaneflare.pages.performer)

(defn node [{:keys [path-params]}]
  (let [{:keys [_handle]} path-params
        dancer {:name "Naomi Gretchen"
                :rating 4.8
                :image "https://via.placeholder.com/300x300?text=Naomi"
                :tags ["VIP Room" "Stage Performer" "Popular"]
                :bio "Known for high-energy stage sets and great conversation. Frequently works weekends at Red Velvet."}
        threads [{:id 123 :title "Great night with Naomi at Red Velvet"}
                 {:id 124 :title "Naomi gave the best lap dance in Vegas"}]]

    [:section.section
     [:div.container

      ;; === Back Button
      [:div.mb-4
       [:a.button.is-light {:href "/#/performers"}
        [:span.icon [:i.fas.fa-arrow-left]]
        [:span "Back to Threads"]]]

      ;; === Profile Header ===
      [:div.columns.is-vcentered
       [:div.column.is-narrow
        [:figure.image.is-128x128
         [:img.is-rounded {:src (:image dancer) :alt (:name dancer)}]]]

       [:div.column
        [:h1.title (:name dancer)]
        [:p.subtitle (str "⭐ " (:rating dancer))]
        [:div.tags
         (for [tag (:tags dancer)]
           [:span.tag.is-info
            {:key tag}
            tag])]]]

      ;; === Bio
      [:div.content.mt-4
       [:p (:bio dancer)]]

      [:hr]

      ;; === Related Threads
      [:h2.title.is-5 "Mentions & Reviews"]
      (if (empty? threads)
        [:p.has-text-grey "No threads found mentioning this dancer."]
        [:ul
         (for [{:keys [id title]} threads]
           [:li [:a {:href (str "/performers/" id)} title]])])]]))
