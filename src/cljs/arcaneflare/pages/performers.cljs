(ns arcaneflare.pages.performers)

(defn performer-card [{:keys [id name thumbnail bio]}]
  [:div.column.is-one-quarter-desktop.is-half-tablet
   [:div.card
    [:div.card-image
     [:figure.image.is-4by3
      [:img {:src thumbnail :alt name}]]]
    [:div.card-content
     [:p.title.is-5 name]
     [:p.subtitle.is-6 (subs bio 0 (min 100 (count bio)))]]
    [:div.card-footer
     [:a.card-footer-item {:href (str "/performers/" id)} "View"]
     [:a.card-footer-item "❤️"]]]]) ; or add toggle logic

(defn node [{:keys [_query-params]}]
  (let [#_#_page (or (get query-params :page) 1)
        performers [{:name "Luna Love"
                     :club "Red Velvet"
                     :specialty "Pole Dancing"
                     :rating 4.5 :bio "Sweet, sassy, and always the life of the party. Let’s have some fun!"
                     :thumbnail "https://ei.phncdn.com/pics/users/0011/1141/2901/avatar95326255/(m=ewILGCjadOf)(mh=1MY8K_QAYgdTxVBQ)200x200.jpg"}
                    {:name "Cherry Blaze"
                     :club "Neon Nights"
                     :specialty "Freestyle"
                     :rating 3.8
                     :bio "Known for her elegance and bold moves, she brings high energy and a hint of mystery to every performance."
                     :thumbnail "https://ei.phncdn.com/pics/users/0025/0867/1561/avatar95398165/(m=ewILGCjadOf)(mh=s07sVGxbzRIxIlHV)200x200.jpg"}]]
    [:section.section
     [:div.container
      [:div.box
       [:div.field.has-addons
        [:div.control
         [:input.input {:type "text" :placeholder "Search performers..."}]]
        [:div.control
         [:button.button.is-info "Search"]]]]
      [:div.columns.is-multiline
       (for [{:keys [id] :as performer} performers]
         ^{:key id} [performer-card performer])]
      [:nav.pagination.is-centered {:role "navigation" :aria-label "pagination"}
       [:a.pagination-previous "Previous"]
       [:a.pagination-next "Next"]]]]))