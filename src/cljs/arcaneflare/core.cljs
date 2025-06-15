(ns arcaneflare.core
  (:require
   [reagent.dom.client :as rdc]
   [reitit.frontend :as rtf]
   [reitit.frontend.controllers :as rfc]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.sections.header :as header]
   [arcaneflare.state :as state]
   [arcaneflare.pages.home :as home-pg]
   [arcaneflare.pages.account :as account-pg]
   [arcaneflare.pages.login :as login-pg]
   [arcaneflare.pages.signup :as signup-pg]
   [arcaneflare.pages.places :as places-pg]
   [arcaneflare.pages.place :as place-pg]
   [arcaneflare.pages.performers :as performers-pg]
   [arcaneflare.pages.performer :as performer-pg]
   [arcaneflare.pages.threads :as threads-pg]
   [arcaneflare.pages.thread :as thread-pg]
   [arcaneflare.token :as token]
   [arcaneflare.http :as http]))

(defonce root-container
  (rdc/create-root
   (.getElementById
    js/document
    "arcaneflare")))

(def routes
  [["/" {:name :route/home
         :view home-pg/node}]
   ["/account" {:name :route/account
                :view account-pg/node}]
   ["/login" {:name :route/login
              :view login-pg/node}]
   ["/signup" {:name :route/signup :view signup-pg/node}]
   ["/places" {:name :route/places
               :view places-pg/node
               :parameters {:query {:page int?}}
               :controllers [{:start places-pg/pull}]}]
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
  [:div
   [header/navbar]
   (when @state/match
     (let [view (-> @state/match :data :view)]
       [view @state/match]))])

(defn pull-member-when-token []
  (when @state/token
    (http/tunnel
     [:api.public.member.root/member-by
      {:member/token @state/token}]
     (fn [member]
       (reset! state/member member))
     (fn [msg]
       (token/remove!)
       (println msg)))))

(defn switch [new]
  (swap! state/match
         (fn [old-match]
           (when new
             (assoc new :controllers
                    (rfc/apply-controllers
                     (:controllers old-match)
                     new))))))

(defn ^:dev/after-load start []
  (rtfe/start! (rtf/router routes) switch
               {:use-fragment true})
  (token/reload!)
  (pull-member-when-token)
  (rdc/render root-container [current-page])
  (println "arcaneflare frontend app started."))

(defn ^:dev/before-load stop []
  (println "arcaneflare frontend app stopped."))

(defn ^:export init []
  (start))

(comment
  (start)
  (stop))
