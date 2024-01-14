(ns tia.components.navbar)

(def logo
  [:a.navbar-brand.d-flex.align-items-center
   {:href "#"}
   [:img.d-inline-block.align-text-top
    {:src "/images/logo.jpg"
     :alt "Logo" :width 30 :height 30}]])

(def toggler
  [:button.navbar-toggler
   {:type "button"
    :data-bs-toggle "collapse"
    :data-bs-target "#navbar-collapse-1"
    :aria-controls "navbar-collapse-1"
    :aria-expanded "false"
    :aria-label "Toggle navigation"}
   [:span.navbar-toggler-icon]])

(defn path
  [{:keys [label href active?]}]
  [:li.nav-item
   [:a.nav-link
    {:class (when active? "active")
     :href href}
    label]])

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
    [:i.fa-solid.fa-heart]]
   [:a.btn.btn-secondary.flex-grow-1.text-nowrap
    {:area-hidden true} 3980]])

(defn in []
  [:a.btn.btn-primary.text-nowrap
   {:href "/docs/download/"}
   "Log In"])

(defn navbar [selection]
  [:nav.navbar.navbar-expand-md.border-bottom.border-secondary.border-opacity-25
   {:style {:background-color "var(--bs-content-bg)"
            :border-bottom "var(--bs-border-width) solid var(--bs-content-border-color)"}}
   [:div.container-fluid.px-3.px-sm-4.px-xl-5.py-1.justify-content-start
    logo toggler
    [:div.collapse.navbar-collapse {:id "navbar-collapse-1"}
     [:ul.navbar-nav.me-auto.mb-2.mb-lg-0
      (path {:label "Club" :active? (= :club selection)
             :href "/clublist/us-northeast"})
      (path {:label "Dancer" :href "/dancerlist"
             :active? (#{:dancer :dancerlist} selection)})
      (path {:label "Review" :href "/reviewlist"
             :active? (#{:review :reviewlist} selection)})
      (path {:label "Discuss" :href "/discusslist"
             :active? (#{:discuss :discusslist} selection)})
      (path {:label "Article" :href "/articlelist"
             :active? (#{:article :articlelist} selection)})
      (path {:label "Gallery" :href "/gallery"
             :active? (#{:gallery} selection)})]
     [:div.d-flex.align-items-center.justify-content-center
      (region) search (counter) (in)]]]])
