(ns tia.pages.club
  (:require
   [clojure.string :as cstr]
   [tia.db.club :as db-club]
   [tia.layout :as layout]
   [malli.core :as m]))

(def attributes
  [:info :event :menu
   :dancer :review :gallery])

(def attributes-enum
  (into [:enum] attributes))

(m/=> content
      [:=> [:cat attributes-enum
            :keyword]
       :any])

(defn content [selection handle]
  (case selection
    :info [:p (str (db-club/find-club-by-handle handle))]
    :event [:h1 "event will be here."]
    :menu [:h1 "menu will be here."]
    :dancer [:h1 "dancer will be here."]
    :review [:h1 "review will be here."]
    :gallery [:h1 "gallery will be here."]))

(m/=> tab
      [:=> [:cat attributes-enum
            attributes-enum :keyword]
       :any])

(defn tab [ident selection handle]
  (let [elems ["/club" (name handle) (name ident)]
        href (cstr/join "/" elems)
        label (-> ident name cstr/capitalize)
        add? (= ident selection)
        adder {:class "active"
               :aria-current "true"}]
    [:li.nav-item
     [:a.nav-link
      (merge {:href href}
             (when add? adder))
      label]]))

(m/=> tabs
      [:=> [:cat attributes-enum
            :keyword]
       :any])

(defn tabs [selection handle]
  (let [f #(tab % selection handle)
        elems (map f attributes)]
    (->> elems
         (cons :ul.nav.nav-pills.ms-auto)
         vec)))

(defn page [selection]
  (fn [req]
    (let [handle (-> req :path-params
                     :handle keyword)
          {:keys [club]}
          (db-club/find-club-by-handle handle)]
      (layout/html
       {:nav {:selection :club}}
       [:div.container-md.px-3.px-sm-4.px-xl-5
        [:div.row
         [:div.py-3.py-sm-4
          [:h1.h3.lh-base.mb-1 (get club :club/label)]
          (tabs selection handle)
          (content selection handle)]]]))))

(def routes
  ["/club/:handle"
   ["/info" {:get (page :info)}]
   ["/event" {:get (page :event)}]
   ["/menu" {:get (page :menu)}]
   ["/dancer" {:get (page :dancer)}]
   ["/review" {:get (page :review)}]
   ["/gallery" {:get (page :gallery)}]])
