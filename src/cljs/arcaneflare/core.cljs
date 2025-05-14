(ns arcaneflare.core
  (:require
   [reagent.core :as r]
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.section.header :as header]
   [arcaneflare.pages.home :as home-pg]
   [arcaneflare.pages.account :as account-pg]
   [arcaneflare.pages.login :as login-pg]
   [arcaneflare.pages.signup :as signup-pg]
   [arcaneflare.pages.area :as area-pg]
   [arcaneflare.pages.clubs :as clubs-pg]
   [arcaneflare.pages.club :as club-pg]
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

(defonce match
  (r/atom nil))

(def routes
  [["/" {:name :page/landing :view home-pg/node}]
   ["/account" {:name :page/account :view account-pg/node}]
   ["/login" {:name :page/login :view login-pg/node}]
   ["/signup" {:name :page/signup :view signup-pg/node}]
   ["/area" {:name :page/area :view area-pg/node}]
   ["/clubs" {:name :page/clubs :view clubs-pg/node
              :parameters {:query {:page int?}}}]
   ["/clubs/:handle" {:name :page/club :view club-pg/node}]
   ["/performers" {:name :page/performers
                   :view performers-pg/node
                   :parameters {:query {:page int?}}}]
   ["/performers/:handle" {:name :page/performer
                           :view performer-pg/node}]
   ["/threads" {:name :page/threads
                :view threads-pg/node
                :parameters {:query {:page int?}}}]
   ["/threads/:handle" {:name :page/thread
                        :view thread-pg/node
                        :parameters {:path {:handle string?}}}]])

(defn current-page []
  [:div.container.is-fullhd
   [header/navbar]
   (when @match
     (let [view (-> @match :data :view)]
       [view @match]))])

(defn ^:dev/after-load start []
  (rtfe/start!
   (rtf/router routes)
   (fn [m]
     (theme/coerce)
     (reset! match m))
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
