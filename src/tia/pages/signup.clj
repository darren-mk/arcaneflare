(ns tia.pages.signup
  (:require
   [tia.layout :as layout]))

(defn form []
  [:form
   [:div
    {:class "row mb-3"}
    [:label
     {:for "full-name-2", :class "col-sm-3 col-form-label mb-1 mb-sm-0"}
     "Full name"]
    [:div
     {:class "col-sm-9"}
     [:input
      {:type "text",
       :class "form-control",
       :id "full-name-2",
       :placeholder "Full name"}]]]
   [:div
    {:class "row mb-3"}
    [:label
     {:for "email", :class "col-sm-3 col-form-label mb-1 mb-sm-0"}
     "Email"]
    [:div
     {:class "col-sm-9"}
     [:input
      {:type "email",
       :class "form-control",
       :id "email",
       :placeholder "Email"}]]]
   [:div
    {:class "row mb-3"}
    [:label
     {:for "password-2", :class "col-sm-3 col-form-label mb-1 mb-sm-0"}
     "Password"]
    [:div
     {:class "col-sm-9"}
     [:input
      {:type "password",
       :class "form-control",
       :id "password-2",
       :placeholder "Password"}]]]
   [:fieldset
    {:class "row mb-3"}
    [:legend
     {:class "col-form-label mb-1 mb-sm-0 col-sm-3 pt-0"}
     "Gender"]
    [:div
     {:class "col-sm-9"}
     [:div
      {:class "form-check"}
      [:input
       {:class "form-check-input",
        :type "radio",
        :name "gender",
        :id "male",
        :value "male",
        :checked ""}]
      [:label {:class "form-check-label", :for "male"} "Male"]]
     [:div
      {:class "form-check"}
      [:input
       {:class "form-check-input",
        :type "radio",
        :name "gender",
        :id "female",
        :value "female"}]
      [:label {:class "form-check-label", :for "female"} "Female"]]
     [:div
      {:class "form-check"}
      [:input
       {:class "form-check-input",
        :type "radio",
        :name "gender",
        :id "other",
        :value "other"}]
      [:label {:class "form-check-label", :for "other"} "Other"]]]]
   [:div
    {:class "row mb-3"}
    [:div
     {:class "col-sm-9 ms-auto"}
     [:div
      {:class "form-check"}
      [:input
       {:class "form-check-input",
        :type "checkbox",
        :id "agree-to-terms-2"}]
      [:label
       {:class "form-check-label", :for "agree-to-terms-2"}
       "I agree to the"
       [:a {:href "#"} "Terms &amp; Conditions"]]]]]
   [:div
    {:class "text-end"}
    [:a {:href "#", :class "btn btn-secondary"} "Cancel"]
    [:button {:type "submit", :class "btn btn-primary"} "Sign up"]]])

(defn page [_]
  (layout/html
   {:nav {:selection nil}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.d-flex.justify-content-center
     (form)]]))

(def routes
  ["/signup" {:get page}])
