(ns tia.components.navbar
  (:require
   [tia.calc :refer [>s]]))

(defn dancers []
  [:li.nav-item.dropdown
   [:a {:class "nav-link dropdown-toggle",
        :href "#",
        :role "button",
        :data-bs-toggle "dropdown",
        :aria-expanded "false"}
    "Dancers"]
   [:ul {:class "dropdown-menu mt-lg-2 rounded-top-0"}
    [:li [:a {:class "dropdown-item"
              :href "#"} "Female Dancers"]]
    [:li [:a {:class "dropdown-item"
              :href "#"} "Male Dancers"]]]])

(defn reviews []
  [:li {:class "nav-item dropdown"}
   [:a {:class "nav-link dropdown-toggle",
        :href "#",
        :role "button",
        :data-bs-toggle "dropdown",
        :aria-expanded "false"}
    "Reviews"]
   [:ul.dropdown-menu.mt-lg-2.rounded-top-0
    [:li [:a {:class "dropdown-item"
              :href "#"} "Page builder"]]
    [:li [:a {:class "dropdown-item"
              :href "#"} "Form builder"]]
    [:li [:hr {:class "dropdown-divider"}]]
    [:li [:a {:class "dropdown-item"
              :href "#"} "Plan and pricing"]]]])

(defn discussions []
  [:li {:class "nav-item dropdown"}
   [:a {:class "nav-link dropdown-toggle",
        :href "#",
        :role "button",
        :data-bs-toggle "dropdown",
        :aria-expanded "false"}
    "Discussions"]
   [:ul {:class "dropdown-menu mt-lg-2 rounded-top-0"}
    [:li [:a {:class "dropdown-item"
              :href "#"} "Page builder"]]
    [:li [:a {:class "dropdown-item"
              :href "#"} "Form builder"]]
    [:li [:hr {:class "dropdown-divider"}]]
    [:li [:a {:class "dropdown-item"
              :href "#"} "Plan and pricing"]]]])

(defn navbar []
  [:nav {:class "navbar navbar-expand-md border-bottom border-secondary border-opacity-25"
         :style {:background-color "var(--bs-content-bg)"
                 :border-bottom "var(--bs-border-width) solid var(--bs-content-border-color)"}}
   [:div {:class "container-fluid px-3 px-sm-4 px-xl-5 py-1 justify-content-start"}
    [:a {:class "navbar-brand d-flex align-items-center"
         :href "#"}
     [:img {:src "..."
            :alt "Logo"
            :width "24"
            :height "24"
            :class "d-inline-block align-text-top"}]
     "Builder"]
    [:button {:class "navbar-toggler"
              :type "button"
              :data-bs-toggle "collapse"
              :data-bs-target "#navbar-collapse-1"
              :aria-controls "navbar-collapse-1"
              :aria-expanded "false"
              :aria-label "Toggle navigation"}
     [:span {:class "navbar-toggler-icon"}]]
    [:div {:class "collapse navbar-collapse"
           :id "navbar-collapse-1"}
     [:ul {:class "navbar-nav me-auto mb-2 mb-lg-0"}
      [:li {:class "nav-item"}
       [:a {:class "nav-link"
            :href "#"} "Clubs"]]
      [:li {:class "nav-item"}
       [:a {:class (>s :nav-link :active)
            :aria-current "page"
            :href "#"}
        "Dancers"]]
      (dancers)
      (reviews)
      (discussions)]
     [:div {:class "d-flex align-items-center justify-content-center"}
      [:div.btn-group.me-1
       [:a {:class "btn btn-secondary flex-grow-1 text-nowrap"
            :target "_blank" :rel "noreferrer"}
        [:i.fa-solid.fa-heart]]
       [:a {:class "btn btn-secondary flex-grow-1 text-nowrap"
            :area-hidden true} 3980]]
      [:a {:href "/docs/download/"
           :class "btn btn-primary text-nowrap ms-1"}
       "Download"]

      #_[:form {:class "d-flex"
                :role "search"}
         [:input {:class "form-control me-2",
                  :type "search",
                  :placeholder "Search docs",
                  :aria-label "Search"}]
         [:button {:class "btn btn-primary"
                   :type "submit"}
          "Search"]]]]]])





  #_[:nav.navbar {:role "navigation"
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
         [:a.button.is-light "Login"]]]]]]
