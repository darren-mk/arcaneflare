(ns arcaneflare.pages.places
  (:require
   [reagent.core :as r]
   [arcaneflare.state :as state]
   [arcaneflare.sections.geographical :as g]
   [arcaneflare.http :as http]))

(defonce items
  (r/atom nil))

(defn card [place]
  [:div.column.is-one-third-tablet.is-one-quarter-desktop
   [:div.card
    [:div.card-image
     [:figure.image.is-4by3
      {:style {:overflow "hidden"}}
      [:img {:src (:thumbnail place)
             :alt (:name place)
             :style {:object-fit "cover"
                     :width "100%"
                     :height "100%"}}]]]
    [:div.card-content
     [:p.title.is-5
      [:a {:href (str "/#/places/" (:handle place))}
       (:name place)]]
     [:p.subtitle.is-6 (:location place)]
     [:p "⭐ "
      (:rating place) " · "
      (:thread-count place)
      " threads"]]]])

(defn content []
  [:div
   (for [{:place/keys [handle name]} @items]
     ^{:key handle}
     [:div [:a {:href (str "/#/places/" handle)}
            name]])])

(defn search-hover-btn []
  (let [active? (r/atom false)]
    (fn []
      [:div.dropdown
       (when @active? {:class [:is-active]})
       [:div.dropdown-trigger
        [:button.button {:aria-haspopup true
                         :on-click #(swap! active? not)}
         [:span "Search"]
         [:span.icon.is-small
          [:i.fas.fa-angle-down {:aria-hidden "true"}]]]]
       [:div.dropdown-menu {:id "dropdown-menu4"
                            :role "menu"}
        [:div.dropdown-content
         [:div.dropdown-item
          [:button
           {:on-click #(println "by area!")}
           "By Area"]]
         [:div.dropdown-item
          [:button
           {:on-click #(println "by name!")}
           "By Name"]]]]])))

(defn extract []
  (reduce
   (fn [acc [_ {place-id :place/id
                geo-id :geo/id}]]
     (cond-> acc
       geo-id (update :geo-ids conj geo-id)
       place-id (update :place-ids conj place-id)))
   {:geo-ids [] :place-ids []} @state/geography))

(defn pull []
  (let [{:keys [geo-ids place-ids]} (extract)
        km [(when (seq geo-ids)
              [:api.public.place/multi-by-geo-ids
               {:geo/ids geo-ids}])
            (when (seq place-ids)
              [:api.public.place/multi-by-ids
               {:place/ids place-ids}])]
        payload (remove nil? km)]
    (http/tunnel
     payload
     #(reset! items (set %))
     #(js/alert "?"))))

(defn node []
  [:div.container
   [g/tags {:reaction pull}]
   [g/bar {:reaction pull}]
   [:div.container.is-flex.is-flex-direction-column
    {:style {:gap 20}}
    (for [{:place/keys [name id]} @items]
      ^{:key id}
      [:div [:h1 (str name)]])]]
  #_[:section.section
     [:div.container

      ;; 🔍 Filter + Search
      [:div.level.mb-5
       [:div.level-left
        [:div.select
         [:select {:value @selected-city
                   :on-change #(reset! selected-city (-> % .-target .-value))}
          [:option {:value ""} "All cities"]
          [:option {:value "seoul"} "Seoul"]
          [:option {:value "busan"} "Busan"]]]]
       [:div.level-right
        [:div.field.has-addons
         [:div.control
          [:input.input {:type "text"
                         :placeholder "Search places"
                         :value @search-text
                         :on-change #(reset! search-text (-> % .-target .-value))}]]
         [:div.control
          [:button.button.is-info "Search"]]]]]

      ;; 🧱 Cards
      [:div.columns.is-multiline
       (for [place (concat dummy-places dummy-places dummy-places)]
         ^{:key (:handle place)}
         [card place])]

      ;; ⏬ Pagination
      [:nav.pagination.is-centered {:role "navigation" :aria-label "pagination"}
       [:a.pagination-previous "Previous"]
       [:a.pagination-next "Next"]
       [:ul.pagination-list
        [:li [:a.pagination-link.is-current {:aria-label "Page 1"} "1"]]
        [:li [:a.pagination-link {:aria-label "Goto page 2"} "2"]]
        [:li [:span.pagination-ellipsis "…"]]
        [:li [:a.pagination-link {:aria-label "Goto page 5"} "5"]]]]]])
