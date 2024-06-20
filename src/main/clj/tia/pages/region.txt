(ns tia.pages.region
  (:require
   [clojure.string :as cstr]
   [tia.data :as data]
   [tia.db.place :as db-place]
   [tia.db.address :as db-address]
   [tia.layout :as layout]
   [tia.model :as model]
   [malli.core :as m]))

(defn item [{:keys [handle label]}]
  (let [href (cstr/join
              "/" ["/club" (name handle) "info"])]
    [:a {:href href}
     label]))

(defn section [state]
  (let [clubs (db-place/find-places-by-state state)]
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
    (layout/page {:nav {:selection :club}}
     [:div.container-md.px-3.px-sm-4.px-xl-5
      [:div.row
       [:div.py-3.py-sm-4
        [:h1.h3.lh-base.mb-1 "Club List"]
        (tabs selection)
        (accordion selection)]]])))

(defn cities [req]
  (let [county-id (-> req :path-params :id parse-uuid)
        cities (db-address/get-cities county-id)]
    (layout/elem
     (into [:div]
           (mapv (fn [{:city/keys [_id label]}]
                   [:button label]) cities)))))

(defn counties [req]
  (let [state-id (-> req :path-params :id parse-uuid)
        counties (db-address/get-counties state-id)]
    (layout/elem
     (into [:div]
           (mapv (fn [{:county/keys [id label]}]
                   [:button
                    {:hx-get (str "/region/cities/" id)
                     :hx-target :#cities}
                    label]) counties)))))

(defn states [req]
  (let [nation-id (-> req :path-params :id parse-uuid)
        states (db-address/get-states nation-id)]
    (layout/elem
     (into [:div]
           (mapv (fn [{:state/keys [id label]}]
                   [:button
                    {:hx-get (str "/region/counties/" id)
                     :hx-target :#counties}
                    label]) states)))))

(defn nations []
  (into
   [:div]
   (mapv (fn [{:nation/keys [id label]}]
           [:button {:hx-get (str "/region/states/" id)
                     :hx-target :#states}
            label])
         (db-address/get-all-nations))))

(defn root-page [_]
  (layout/page
   {}
   [:div
    [:div {:id :nations} (nations)]
    [:div {:id :states}]
    [:div {:id :counties}]
    [:div {:id :cities}]]))

(def routes
  ["/region"
   ["" {:get root-page}]
   ["/states/:id" {:get states}]
   ["/counties/:id" {:get counties}]
   ["/cities/:id" {:get cities}]
   #_#_#_#_#_#_
   ["" {:get (page :us-northeast)}]
   ["/us-northeast" {:get (page :us-northeast)}]
   ["/us-midwest" {:get (page :us-midwest)}]
   ["/us-south" {:get (page :us-south)}]
   ["/us-west" {:get (page :us-west)}]
   ["/canada" {:get (page :canada)}]])
