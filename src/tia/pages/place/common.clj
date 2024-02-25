(ns tia.pages.place.common
  (:require
   [clojure.string :as cstr]
   [tia.calc :as c]
   [tia.layout :as l]))

(def uri
  "/place")

(def review-uri
  "/reviews")

(def selections
  [:info :event :menu
   :dancer :reviews :gallery])

(defn tab [ident selection place]
  (let [handle (:place/handle place)
        href (c/path uri handle ident)
        label (-> ident name cstr/capitalize)
        selected? (= ident selection)]
    [:li.nav-item
     [:a.nav-link {:href href
                   :class (if selected? "active" nil)
                   :aria-current selected?}
      label]]))

(defn tabs [selection place]
  (let [f #(tab % selection place)
        elems (mapv f selections)]
    (into [:ul.nav.nav-tabs]
          elems)))

(defn paginate [selection component]
  (fn [{:keys [session place]}]
    (l/page
     {:nav {:selection :club}
      :session session}
     [:div.container-md.px-3.px-sm-4.px-xl-5
      [:div.row
       [:div.py-3.py-sm-4
        [:h1.h3.lh-base.mb-1 (:place/label place)]
        (tabs selection place)
        (component place)]]])))
