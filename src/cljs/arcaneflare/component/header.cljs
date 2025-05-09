(ns arcaneflare.component.header
  (:require
   [reagent.core :as r]))

(defonce is-modal-open?
  (r/atom false))

(def close-modal
   #(reset! is-modal-open? false))

(def toggle-modal
  #(swap! is-modal-open? not))

(defn modal-for-mobile []
  [:div.modal
   (merge {:style {:justify-content "flex-start"}}
          (when @is-modal-open? {:class :is-active}))
   [:div.modal-background
    {:on-click close-modal}]
   [:div.modal-content
    [:div.box.mx-4 {:style {:margin-top 80}}
     [:p "Modal JS example"]]]
   [:button.modal-close.is-large
    {:aria-label "close"
     :on-click close-modal}]])

(defn burger []
  [:a.navbar-burger
   {:role :button
    :aria-label "menu"
    :aria-expanded true
    :data-target "navbarMain"
    :on-click toggle-modal}
   (for [_ (range 4)] [:span {:aria-hidden true}])])

(defn logo []
  [:strong "StripClubReviews"])

(defn add-club-btn []
  [:a.button.is-primary {:href "/add-club"}
   [:strong "Add a Club"]])

(defn theme-toggle-btn []
  [:button.button
   [:span.icon [:i.fas.fa-adjust]]])

(defn navbar []
  [:nav.navbar {:role "navigation"
                :aria-label "main navigation"}
   [modal-for-mobile]
   [:div.navbar-brand
    [:a.navbar-item {:href "/"} [logo]]
    (when-not @is-modal-open? [burger])]
   [:div.navbar-menu
    [:div.navbar-end
     [:div.navbar-item.has-dropdown.is-hoverable
      [:a.navbar-link "Location"]
      [:div.navbar-dropdown
       [:a.navbar-item "New York"]
       [:a.navbar-item "Las Vegas"]
       [:a.navbar-item "Miami"]
       [:hr.navbar-divider]
       [:a.navbar-item "More..."]]]
     [:div.navbar-item.mr-6
      [:div.field.has-addons
       [:p.control
        [:input.input {:type "text"
                       :placeholder "Search clubs or tags..."}]]
       [:p.control
        [:button.button.is-info
         [:span.icon [:i.fas.fa-search]]]]]]
     [:div.navbar-item [add-club-btn]]
     [:div.navbar-item [theme-toggle-btn]]
     [:div.navbar-item.has-dropdown.is-hoverable
      [:a.navbar-link "Profile"]
      [:div.navbar-dropdown.is-right
       [:a.navbar-item "My Reviews"]
       [:a.navbar-item "Settings"]
       [:hr.navbar-divider]
       [:a.navbar-item "Log out"]]]]]])