(ns arcaneflare.pages.login
  (:require
   [reagent.core :as r]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.http :refer [tunnel]]
   [arcaneflare.token :as tk]
   [arcaneflare.state.controlled :as c.state]
   [arcaneflare.utils :refer [reset-tv!]]))

(defonce username-typed
  (r/atom nil))

(defonce passcode-typed
  (r/atom nil))

(defn title []
  [:h1.title.is-4.has-text-centered
   "Login"])

(defn username []
  [:div.field
   [:label.label "Username"]
   [:div.control
    [:input.input
     {:type "text"
      :placeholder "Enter username"
      :value @username-typed
      :on-change (reset-tv! username-typed)}]]])

(defn passcode []
  [:div.field
   [:label.label "Password"]
   [:div.control
    [:input.input
     {:type "password"
      :placeholder "Enter password"
      :value @passcode-typed
      :on-change (reset-tv! passcode-typed)}]]])

(defn forgot []
  [:div.has-text-centered
   [:a {:href "#"}
    "Forgot your password?"]])

(defn submit []
  (println "logging in with "
           @username-typed ", "
           @passcode-typed)
  (tunnel [:api.public.member.root/login!
           {:member/username @username-typed
            :member/passcode @passcode-typed}]
          (fn [token]
            (when token
              (tk/new token)
              (rtfe/push-state :route/home)))
          (fn [msg] (assoc c.state/errors :login msg))))

(defn submission []
  [:div.field.mt-4
   [:div.control
    [:button.button.is-primary.is-fullwidth
     {:on-click submit}
     "Log In"]]])

(defn signup []
  [:p.has-text-centered.mt-2
   [:span "Don’t have an account? "]
   [:a {:href "/#/signup"} "Sign up"]])

(defn node []
  [:section.section
   {:style {:margin-top "15%"}}
   [:div.columns.is-centered
    [:div.column.is-one-third
     [:div.box
      [title]
      [username]
      [passcode]
      [submission]
      [forgot]
      [signup]]]]])