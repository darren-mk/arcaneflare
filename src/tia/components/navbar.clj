(ns tia.components.navbar
  (:require
   [tia.data :as d]))

(def hrefs
  {:places (:places d/uri)
   :dancer "/dancerlist"
   :dancerlist "/dancerlist"
   :review (:review d/uri)
   :reviewlist "/reviewlist"
   :discuss "/discusslist"
   :discusslist "/discusslist"
   :article "/articlelist"
   :articlelist "/articlelist"
   :gallery "/gallery"})

(def labels
  {:places "Places"
   :club "Club"
   :dancer "Dancer"
   :dancerlist "Dancer"
   :review "Review"
   :reviewlist "Review"
   :discuss "Discuss"
   :discusslist "Discuss"
   :article "Article"
   :articlelist "Article"
   :gallery "Gallery"})

(def logo
  [:a.navbar-brand.d-flex.align-items-center
   {:href "#"}
   [:img.d-inline-block.align-text-top
    {;;:src "/images/logo.jpg"
     :alt "Logo" :width 30 :height 30}]])

(def toggler
  [:button.navbar-toggler
   {:type :button
    :data-bs-toggle :collapse
    :data-bs-target "#navbar-collapse-1"
    :aria-controls "navbar-collapse-1"
    :aria-expanded "false"
    :aria-label "Toggle navigation"}
   [:span.navbar-toggler-icon]])

(defn path [idents selection]
  (let [active? (contains? idents selection)
        cls (when active? "active")
        href (get hrefs (first idents))
        label (get labels (first idents))]
    [:li.nav-item
     [:a.nav-link
      {:class cls :href href}
      label]]))

(defn region []
  [:div.btn-group.me-2
   [:a.btn.btn-secondary.d-flex.align-items-center
    {:href "/region"}
    [:i.fa-solid.fa-map]]
   [:a.btn.btn-secondary.flex-grow-1.text-nowrap
    {:area-hidden true} "Everywhere"]])

(def search
  [:input.form-control.me-2
   {:type "search"
    :placeholder "Search docs"
    :aria-label "Search"}])

(defn counter []
  [:div.btn-group.me-2
   [:a.btn.btn-secondary.d-flex.align-items-center
    {:target :_blank :rel :noreferrer}
    d/heart-outlined-icon]
   [:a.btn.btn-secondary.flex-grow-1.text-nowrap
    {:area-hidden true} 3980]])

(defn gate [session person]
  (if (and session person)
    [:div {:class "dropdown"}
     [:button {:class "btn btn-secondary dropdown-toggle"
               :type "button"
               :data-bs-toggle "dropdown"
               :aria-expanded "false"}
      (:person/nickname person)]
     [:ul {:class "dropdown-menu"}
      [:li [:a {:class "dropdown-item"
                :href "#"}
            "Pofile"]]
      [:li [:a {:class "dropdown-item"
                :href "#"}
            "Another action"]]
      [:li [:a {:class "dropdown-item"
                :href "/logout"}
            "Log Out"]]]]
    [:div
     [:a.btn.btn-primary.text-nowrap
      {:href "/login"} "Log In"]]))

(defn navbar [{:keys [person session nav]}]
  (let [selection (:selection nav)]
    [:nav.navbar.navbar-expand-md.border-bottom.border-secondary.border-opacity-25
     {:style {:background-color "var(--bs-content-bg)"
              :border-bottom "var(--bs-border-width) solid var(--bs-content-border-color)"}}
     [:div.container-fluid.px-3.px-sm-4.px-xl-5.py-1.justify-content-start
      logo toggler
      [:div.collapse.navbar-collapse {:id "navbar-collapse-1"}
       [:ul.navbar-nav.me-auto.mb-2.mb-lg-0
        (path #{:places} selection)
        (path #{:dancerlist} selection)
        (path #{:review :reviewlist} selection)
        (path #{:discuss :discusslist} selection)
        (path #{:article :articlelist} selection)
        (path #{:gallery} selection)]
       [:div.d-flex.align-items-center.justify-content-center
        (region) search (counter) (gate session person)]]]]))
