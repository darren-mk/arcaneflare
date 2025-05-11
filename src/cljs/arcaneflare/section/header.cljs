(ns arcaneflare.section.header
  (:require
   [reagent.core :as r]
   [arcaneflare.state :as state]))

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
  [:strong "SCR"])

(defn theme-toggle-btn []
  [:span.icon {:on-click state/toggle-theme}
   (case @state/theme
     :dark [:i.fas.fa-lg.fa-moon
            {:style {:color "hsl(256, 89%, 65%)"}}]
     :light [:i.fas.fa-lg.fa-sun
             {:style {:color "hsl(42, 100%, 53%)"}}])])

(defn location-nav-item []
  [:a {:href "/#/location"}
   [:span.icon.is-large
    {:on-click (fn [] (if (seq @state/location)
                        (reset! state/location nil)
                        (reset! state/location #{"yo"})))}
    (if (seq @state/location)
      [:i.fa-lg.fa-solid.fa-street-view
       {:style {:color "hsl(42, 100%, 53%)"}}]
      [:i.fa-lg.fa-solid.fa-earth-americas
       {:style {:color "hsl(256, 89%, 65%)"}}])]])

(defn profile []
  [:a {:href "/#/account"}
   [:span.icon.is-large.is-link
    [:i.fa-lg.fa-regular.fa-circle-user
     {:style {:color "hsl(256, 89%, 65%)"}}]]])

(defn navbar []
  [:nav.navbar.mt-1.mb-6
   {:role "navigation"
    :aria-label "main navigation"}
   [modal-for-mobile]
   [:div.navbar-brand
    [:a.navbar-item {:href "/#/"} [logo]]
    (when-not @is-modal-open? [burger])]
   [:div.navbar-menu
    [:div.navbar-end
     [:div.navbar-item [location-nav-item]]
     [:div.navbar-item [theme-toggle-btn]]
     [:div.navbar-item [profile]]]]])