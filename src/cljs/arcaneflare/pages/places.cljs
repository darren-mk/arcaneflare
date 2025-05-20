(ns arcaneflare.pages.places
  (:require
   [reagent.core :as r]))

(defonce search-text
  (r/atom nil))

(defonce selected-city
  (r/atom nil))

(defonce dummy-places
  [{:handle "a1b2c3-skyroom"
    :name "Skyroom"
    :location "Gangnam, Seoul"
    :rating 4.6
    :thread-count 12
    :thumbnail "https://cdn.nysapphire.com/wp-content/uploads/2024/05/29191727/SapphireOnly-1.png"}
   {:handle "d4e5f6-midnight"
    :name "Midnight Club"
    :location "Itaewon, Seoul"
    :rating 4.2
    :thread-count 8
    :thumbnail "https://www.rickschicago.com/images/sites/34/hero.mp4"}
   {:handle "g7h8i9-moonlight"
    :name "Moonlight"
    :location "Busan"
    :rating 4.8
    :thread-count 15
    :thumbnail "https://www.rickschicago.com/images/sites/34/1909/ricks7.JPG"}])

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
      [:a {:href (str "/places/" (:handle place))}
       (:name place)]]
     [:p.subtitle.is-6 (:location place)]
     [:p "⭐ "
      (:rating place) " · "
      (:thread-count place)
      " threads"]]]])

(defn node []
  [:section.section
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
