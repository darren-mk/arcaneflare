(ns arcaneflare.pages.signup
  (:require
   [reagent.core :as r]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.http :refer [tunnel]]
   [arcaneflare.utils :refer [reset-tv!]]))

(defonce username-typed
  (r/atom nil))

(defonce email-typed
  (r/atom nil))

(defonce role-selected
  (r/atom "customer"))

(defonce passcode-typed
  (r/atom nil))

(defonce confirm-passcode-typed
  (r/atom nil))

(defn title []
  [:h1.title.is-4.has-text-centered "Sign Up"])

(defn username []
  [:div.field
   [:label.label "Username"]
   [:div.control
    [:input.input
     {:type "text"
      :placeholder "Choose a username"
      :value @username-typed
      :on-change (reset-tv! username-typed)}]]])

(defn email []
  [:div.field
   [:label.label "Email"]
   [:div.control
    [:input.input
     {:type "email"
      :placeholder "Enter your email"
      :value @email-typed
      :on-change (reset-tv! email-typed)}]]])

(defn role []
  [:div.field
   [:label.label "Role"]
   [:div.control
    [:div.select.is-fullwidth
     [:select {:value @role-selected
               :on-change (reset-tv! role-selected)}
      [:option {:value "performer"} "Performer"]
      [:option {:value "customer"} "Customer"]
      [:option {:value "staff"} "Staff"]
      [:option {:value "admin"} "Admin"]]]]])

(defn passcode []
  [:div.field
   [:label.label "Password"]
   [:div.control
    [:input.input
     {:type "password"
      :placeholder "Choose a password"
      :value @passcode-typed
      :on-change (reset-tv! passcode-typed)}]]])

(defn confirm-passcode []
  [:div.field
   [:label.label "Confirm Password"]
   [:div.control
    [:input.input
     {:type "password"
      :placeholder "Re-enter your password"
      :value @confirm-passcode-typed
      :on-change (reset-tv! confirm-passcode-typed)}]]])

(defn login-link []
  [:p.has-text-centered.mt-2
   [:span "Already have an account? "]
   [:a {:href "/#/login"} "Log in"]])

(defn submit []
  (let [payload {:username @username-typed
                 :email @email-typed
                 :passcode @passcode-typed
                 :role @role-selected}]
    (tunnel [:api.public.member.root/insert! [payload]]
            (fn [_resp] (rtfe/push-state :route/login))
            (fn [resp] (js/alert resp)))))

(defn submission []
  [:div.field.mt-4
   [:div.control
    [:button.button.is-primary.is-fullwidth
     {:on-click submit}
     "Sign Up"]]])

(defn node []
  [:section.section
   {:style {:margin-top "15%"}}
   [:div.columns.is-centered
    [:div.column.is-one-third
     [:div.box
      [title]
      [username]
      [role]
      [email]
      [passcode]
      [confirm-passcode]
      [submission]
      [login-link]]]]])