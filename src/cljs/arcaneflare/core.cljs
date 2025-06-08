(ns arcaneflare.core
  (:require
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.section.header :as header]
   [arcaneflare.state :as state]
   [arcaneflare.pages.home :as home-pg]
   [arcaneflare.pages.account :as account-pg]
   [arcaneflare.pages.login :as login-pg]
   [arcaneflare.pages.signup :as signup-pg]
   [arcaneflare.pages.area :as area-pg]
   [arcaneflare.pages.places :as places-pg]
   [arcaneflare.pages.place :as place-pg]
   [arcaneflare.pages.performers :as performers-pg]
   [arcaneflare.pages.performer :as performer-pg]
   [arcaneflare.pages.threads :as threads-pg]
   [arcaneflare.pages.thread :as thread-pg]
   [arcaneflare.theme :as theme]))

(defonce root-container
  (rdc/create-root
   (.getElementById
    js/document
    "arcaneflare")))

(def routes
  [["/" {:name :route/home :view home-pg/node}]
   ["/account" {:name :route/account :view account-pg/node}]
   ["/login" {:name :route/login :view login-pg/node}]
   ["/signup" {:name :route/signup :view signup-pg/node}]
   ["/area" {:name :route/area :view area-pg/node}]
   ["/places" {:name :route/places
               :view places-pg/node
               :parameters {:query {:page int?}}}]
   ["/places/:handle" {:name :route/place
                       :view place-pg/node}]
   ["/performers" {:name :route/performers
                   :view performers-pg/node
                   :parameters {:query {:page int?}}}]
   ["/performers/:handle" {:name :route/performer
                           :view performer-pg/node}]
   ["/threads" {:name :route/threads
                :view threads-pg/node
                :parameters {:query {:page int?}}}]
   ["/threads/:handle" {:name :route/thread
                        :view thread-pg/node
                        :parameters {:path {:handle string?}}}]])

(defn current-page []
  [:div.container.is-fullhd
   [header/navbar]
   (when @state/match
     (let [view (-> @state/match :data :view)]
       [view @state/match]))])

(defn ^:dev/after-load start []
  (rtfe/start!
   (rtf/router routes)
   (fn [m]
     (theme/ensure)
     (reset! state/match m))
   {:use-fragment true})
  (rdc/render
   root-container
   [current-page])
  (println "arcaneflare frontend app started."))

(defn ^:dev/before-load stop []
  (println "arcaneflare frontend app stopped."))

(defn ^:export init []
  (start))

(comment
  (start)
  (stop))
