(ns tia.pages.place
  (:require
   [clojure.string :as cstr]
   [tia.db.place :as db-place]
   [tia.layout :as layout]
   [malli.core :as m]))

(def uri
  "/place")

(def selections
  [:info :event :menu
   :dancer :review :gallery])

(def selections-enum
  (into [:enum] selections))

(defn info [{:place/keys [label nudity]}
            {:address/keys [street city state country]}]
  [:div
   [:p label]
   [:p (cstr/join " " ["Nudity: " nudity])]
   [:p (cstr/join " " ["Address: " street city state country])]])

(defn content [selection handle]
  (let [{:keys [place address]}
        (db-place/find-place-and-address handle)]
    (case selection
      :info (info place address)
      :event [:h1 "event will be here."]
      :menu [:h1 "menu will be here."]
      :dancer [:h1 "dancer will be here."]
      :review [:h1 "review will be here."]
      :gallery [:h1 "gallery will be here."])))

(defn tab [ident selection handle]
  (let [elems ["/place" (name handle) (name ident)]
        href (cstr/join "/" elems)
        label (-> ident name cstr/capitalize)
        selected? (= ident selection)]
    [:li.nav-item
     [:a.nav-link {:href href
                   :class (if selected? "active" nil)
                   :aria-current selected?}
      label]]))

(defn tabs [selection handle]
  (let [f #(tab % selection handle)
        elems (mapv f selections)]
    (into [:ul.nav.nav-tabs]
          elems)))

(m/=> page
      [:=> [:cat selections-enum]
       :any])

(defn page [selection]
  (fn [{:keys [session] :as req}]
    (let [handle (-> req :path-params
                     :handle keyword)
          {:keys [club]}
          (db-place/find-place-and-address handle)]
      (layout/frame
       {:nav {:selection :club}
        :session session}
       [:div.container-md.px-3.px-sm-4.px-xl-5
        [:div.row
         [:div.py-3.py-sm-4
          [:h1.h3.lh-base.mb-1 (get club :place/label)]
          (tabs selection handle)
          (content selection handle)]]]))))

(def routes
  [(str uri "/:handle")
   ["/info" {:get (page :info)}]
   ["/event" {:get (page :event)}]
   ["/menu" {:get (page :menu)}]
   ["/dancer" {:get (page :dancer)}]
   ["/review" {:get (page :review)}]
   ["/gallery" {:get (page :gallery)}]])
