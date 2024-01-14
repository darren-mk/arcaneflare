(ns tia.pages.region
  (:require
   [clojure.string :as cstr]
   [tia.data :as data]
   [tia.db.club :as club]
   [tia.layout :as layout]
   [tia.model :as model]
   [malli.core :as m]))

(defn item [{:keys [handle label]}]
  (let [href (cstr/join
              "/" ["/club" (name handle) "info"])]
    [:a {:href href}
     label]))

(m/=> section
      [:=> [:cat model/state]
       :any])

(defn section [state]
  (let [clubs (club/find-clubs-by-state state)]
    [:div.accordion-item
     [:h2.accordion-header
      [:button.accordion-button.collapsed
       {:type "button",
        :data-bs-toggle "collapse",
        :data-bs-target (str "#" (name state))
        :aria-expanded "false",
        :aria-controls (name state)}
       (-> data/states state :label)]]
     [:div.accordion-collapse.collapse
      {:id (name state)
       :data-bs-parent "#accordion-example-1"}
      [:div.accordion-body
       (into
        [:div.d-flex.justify-content-start.flex-wrap.me-auto]
        (mapv item clubs))]]]))

(m/=> accordion
      [:=> [:cat model/division]
       :any])

(defn accordion [division]
  (vec (concat
        [:div.accordion
         {:id "accordion-example-1"}]
        (map section (-> data/divisions division :states)))))

(m/=> tab
      [:=> [:cat model/division model/division]
       :any])

(defn tab [division selection]
  [:li.nav-item
   [:a.nav-link
    (merge {:href (str "/clublist/"
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
  (fn [_]
    (layout/html {:nav {:selection :club}}
     [:div.container-md.px-3.px-sm-4.px-xl-5
      [:div.row
       [:div.py-3.py-sm-4
        [:h1.h3.lh-base.mb-1 "Club List"]
        (tabs selection)
        (accordion selection)]]])))

(def routes
  ["/region"
   ["" {:get (page :us-northeast)}]
   ["/us-northeast" {:get (page :us-northeast)}]
   ["/us-midwest" {:get (page :us-midwest)}]
   ["/us-south" {:get (page :us-south)}]
   ["/us-west" {:get (page :us-west)}]
   ["/canada" {:get (page :canada)}]])
