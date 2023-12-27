(ns tia.components.navbar)

(defn navbar []
  [:nav.navbar {:role "navigation"
                :aria-label "main navigation"}
   [:div.navbar-brand
    [:a.navbar-item {:href "https://bulma.io"}
     [:img {:src "https://bulma.io/images/bulma-logo.png"
            :width "112"
            :height "28"}]]
    [:a.navbar-burger
     {:role "button"
      :aria-label "menu"
      :aria-expanded "false"
      :data-target "navbarBasicExample"}
     [:span {:aria-hidden "true"}]
     [:span {:aria-hidden "true"}]
     [:span {:aria-hidden "true"}]]]
   [:div.navbar-menu {:id "navbarBasicExample"}
    [:div.navbar-start
     [:a.navbar-item "Clubs"]
     [:a.navbar-item "Dancers"]
     [:a.navbar-item "Reviews"]
     [:a.navbar-item "Discussions"]
     [:a.navbar-item "Articles"]
     [:a.navbar-item "Gallery"]
     [:div.navbar-item.has-dropdown.is-hoverable
      [:a.navbar-link  "More"]
      [:div
       {:class "navbar-dropdown"}
       [:a.navbar-item "TBD 1"]
       [:a.navbar-item "TBD 2"]
       [:a.navbar-item "Contact"]
       [:hr.navbar-divider]
       [:a.navbar-item "Report an issue"]]]]
    [:div.navbar-end
     [:div.navbar-item
      [:div.buttons
       [:a.button.is-primary
        [:strong "Join"]]
       [:a.button.is-light "Login"]]]]]])
