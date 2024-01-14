(ns tia.pages.login
  (:require
   [tia.layout :as layout]))

(defn form []
  [:form
   [:div.mb-3
    [:label.form-label
     {:for :username}
     "Username"]
    [:input.form-control
     {:type "text"
      :id "username"
      :required ""
      :placeholder "Username"}]]
   [:div.mb-3
    [:label.form-label
     {:for :password}
     "Password"]
    [:input
     {:type "password"
      :class "form-control"
      :id "password"
      :required ""
      :placeholder "Password"}]]
   [:div.mb-3.pb-3.border-bottom
    [:button.btn.btn-primary.w-100
     {:type :submit} "Sign in"]]
   [:div.text-center.text-body-secondary
    "Don't have an account?"
    [:a {:href "/signup"} "Sign up"]]])

(defn page [_]
  (layout/html
   {:nav {:selection nil}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.d-flex.justify-content-center
     (form)]]))

(def routes
  ["/login" {:get page}])
