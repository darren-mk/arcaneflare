(ns tia.pages.places
  (:require
   [clojure.string :as cstr]
   [tia.calc :as calc]
   [tia.data :as data]
   [tia.db.address :as db-address]
   [tia.db.place :as db-place]
   [tia.layout :as layout]
   [tia.model :as model]
   [tia.pages.place.core :as place]
   [malli.core :as m]))

(def uri
  "/places")

(defn item [{:keys [place/label place/handle]}]
  (let [path [place/uri (name handle) "info"]
        href (cstr/join "/" path)]
    [:li [:a {:href href} label]]))

(defn paragraph [city]
  (let [city-id (calc/idify city)
        places (db-place/find-places-in-city city)]
    [:div
     [:button.btn.btn-link
      {:type :button
       :data-bs-toggle :collapse
       :data-bs-target (str "#" city-id)
       :aria-expanded false
       :aria-controls city-id}
      city
      [:i.fa-light.fa-angle-down]]
     [:div.collapse {:id city-id}
      [:div.ps-4.mt-2
       (into [:ul.list-unstyled.mb-0]
             (mapv item places))]]]))

(defn section [state]
  (let [state-arg (if (keyword? state)
                    (-> state name cstr/upper-case)
                    state)
        cities (db-address/find-cities-in-state
                state-arg)]
    [:div.accordion-item
     [:h2.accordion-header
      [:button.accordion-button.collapsed
       {:type :button
        :data-bs-toggle :collapse
        :data-bs-target (str "#" (name state))
        :aria-expanded false
        :aria-controls (name state)}
       (-> state data/states :label)]]
     [:div.accordion-collapse.collapse
      {:id (name state)
       :data-bs-parent "#accordion-example-1"}
      (into [:div.accordion-body]
            (mapv paragraph cities))]]))

(m/=> accordion
      [:=> [:cat model/division]
       :any])

(defn accordion [division]
  (let [sections (-> data/divisions
                     division :states)]
    (vec (concat
          [:div#accordion.accordion]
          (map section sections)))))

(defn tab [division selection]
  [:li.nav-item
   [:a.nav-link
    (merge {:href (str uri "/"
                       (name division))}
           (when (= division selection)
             {:class "active"
              :aria-current "true"}))
    (-> data/divisions division :label)]])

(m/=> tabs
      [:=> [:cat model/division]
       :any])

(defn tabs [selection]
  [:ul.nav.nav-pills.ms-auto
   (tab :us-northeast selection)
   (tab :us-midwest selection)
   (tab :us-south selection)
   (tab :us-west selection)
   (tab :canada selection)])

(m/=> page
      [:=> [:cat model/division]
       :any])

(defn page [selection]
  (fn [{:keys [session person] :as _req}]
    (layout/page
     {:nav {:selection :club}
      :session session
      :person person}
     [:div.container-md.px-3.px-sm-4.px-xl-5
      [:div.row
       [:div.py-3.py-sm-4
        [:h1.h3.lh-base.mb-1 "Club List"]
        (tabs selection)
        (accordion selection)]]])))

(def routes
  [uri
   ["" {:get (page :us-northeast)}]
   ["/us-northeast" {:get (page :us-northeast)}]
   ["/us-midwest" {:get (page :us-midwest)}]
   ["/us-south" {:get (page :us-south)}]
   ["/us-west" {:get (page :us-west)}]
   ["/canada" {:get (page :canada)}]])
