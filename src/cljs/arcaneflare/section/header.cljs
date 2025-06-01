(ns arcaneflare.section.header
  (:require
   [reagent.core :as r]
   [arcaneflare.state :as state]
   [arcaneflare.theme :as theme]))

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
   (for [i (range 4)]
     [:span {:key i
             :aria-hidden true}])])

(defn logo []
  [:strong "SCR"])

(defn theme-toggle-btn []
  [:span.icon {:on-click theme/toggle}
   (case (theme/bring)
    :dark [:i.fas.fa-lg.fa-moon
           {:style {:color "hsl(256, 89%, 65%)"}}]
    :light [:i.fas.fa-lg.fa-sun
            {:style {:color "hsl(42, 100%, 53%)"}}])])

(defn area-nav-item []
  [:a {:href "/#/area"}
   [:span.icon.is-large
    {:on-click (fn [] (if (seq @state/areas)
                        (reset! state/areas nil)
                        (reset! state/areas #{"yo"})))}
    (if (seq @state/areas)
      [:i.fa-lg.fa-solid.fa-street-view
       {:style {:color "hsl(42, 100%, 53%)"}}]
      [:i.fa-lg.fa-solid.fa-earth-americas
       {:style {:color "hsl(256, 89%, 65%)"}}])]])

(defn profile []
  (if @state/token
    [:a {:href "/#/account"}
     [:span.icon.is-large.is-link
      [:i.fa-lg.fa-regular.fa-circle-user
       {:style {:color "hsl(256, 89%, 65%)"}}]]]
    [:a {:href "/#/login"}
     [:span.icon.is-large.is-link
      [:i.fa-lg.fa-solid.fa-arrow-right-to-bracket
       {:style {:color "hsl(256, 89%, 65%)"}}]]]))

(defn brand []
  [:div.navbar-brand
   [:a.navbar-item {:href "/#/"} [logo]]
   (when-not @is-modal-open? [burger])])

(defn places []
  [:a {:href "/#/places"}
   "Places"])

(defn performers []
  [:a {:href "/#/performers"}
   "Performers"])

(defn threads []
  [:a {:href "/#/threads"}
   "Threads"])

(defn navbar []
  [:nav.navbar.mt-1.mb-2
   {:role "navigation"
    :aria-label "main navigation"}
   [:div.container
    [modal-for-mobile]
    [brand]
    [:div.navbar-menu
     [:div.navbar-start
      [:div.navbar-item [places]]
      [:div.navbar-item [performers]]
      [:div.navbar-item [threads]]]
     [:div.navbar-end
      [:div.navbar-item [area-nav-item]]
      [:div.navbar-item [theme-toggle-btn]]
      [:div.navbar-item [profile]]]]]])
