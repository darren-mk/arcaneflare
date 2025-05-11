(ns arcaneflare.pages.club)

(defn node [{:keys [path-params]}]
  (let [{:keys [#_handle]} path-params
        club {:name "Red Velvet"
              :rating 4.2
              :location "123 Sunset Blvd, Las Vegas, NV"
              :image "https://via.placeholder.com/600x300?text=Red+Velvet"
              :tags ["VIP" "Open Late" "Couples Welcome"]
              :description "Red Velvet is a high-end club known for its luxurious interior, consistent stage shows, and friendly staff."}
        threads [{:id 201 :title "Best night ever at Red Velvet"}
                 {:id 202 :title "Prices were fair and vibe was perfect"}]]

    [:section.section
     [:div.container

      ;; === Back Button
      [:div.mb-4
       [:a.button.is-light {:href "/#/clubs"}
        [:span.icon [:i.fas.fa-arrow-left]]
        [:span "Back to Threads"]]]

      ;; === Club Header ===
      [:h1.title (:name club)]
      [:p.subtitle (str "⭐ " (:rating club))]

      ;; === Image
      [:figure.image.is-16by9
       [:img {:src (:image club) :alt (:name club)}]]

      ;; === Location & Tags
      [:div.mt-4
       [:p.has-text-weight-semibold "Location:"]
       [:p (:location club)]
       [:div.tags.mt-2
        (for [tag (:tags club)]
          [:span.tag.is-primary tag])]]

      ;; === Description
      [:div.content.mt-5
       [:h2.title.is-5 "About"]
       [:p (:description club)]]

      ;; === Map (placeholder)
      [:div.box.mt-4
       [:p.has-text-grey "🗺️ Map would go here"]]

      [:hr]

      ;; === Related Threads
      [:h2.title.is-5 "Mentions & Reviews"]
      (if (empty? threads)
        [:p.has-text-grey "No threads yet about this club."]
        [:ul
         (for [{:keys [id title]} threads]
           [:li [:a {:href (str "/threads/" id)} title]])])]]))
