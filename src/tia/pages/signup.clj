(ns tia.pages.signup
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.db.person :as pdb]
   [tia.layout :as l]
   [tia.model :as md]
   [tia.calc :refer [>s]]))

(defn input [k]
  (let [target (str (name k) "check-result")
        label (-> k name
                  cstr/capitalize)
        checker {:hx-get (str "/signup/check-" (name k))
                 :hx-trigger (>s :keyup :changed :delay:500ms)
                 :hx-target (str "#" target)}]
    [:div.row.mb-3
     [:label.col-sm-3.col-form-label.mb-1.mb-sm-0
      {:for k} label]
     [:div.col-sm-9
      [:input.form-control
       (merge {:name k :type k :id k
               :required true
               :placeholder label}
              checker)]
      [:div {:id target}]]]))

(defn role [k]
  [:div.form-check
   [:input.form-check-input
    {:name :role
     :type :radio
     :required true
     :value k}]
   [:label.form-check-label
    (-> k name cstr/capitalize)]])

(def agreement
  [:div.row.mb-3
   [:div.col-sm-9.ms-auto
    [:div.form-check
     [:input.form-check-input
      {:type :checkbox
       :required true}]
     [:label.form-check-label
      {:for "agree-to-terms-2"}
      "I agree to the"
      [:a {:href "#"}
       "Terms &amp; Conditions"]]]]])

(def control
  [:div.text-end
   [:a.btn.btn-secondary
    {:href "#"} "Cancel"]
   [:button.btn.btn-primary
    {:type :submit} "Sign up"]])

(defn form []
  [:form {:action "/signup"
          :method :post}
   (input :nickname)
   (input :email)
   (input :password)
   [:fieldset.row.mb-3
    [:legend.col-form-label.mb-1.mb-sm-0.col-sm-3.pt-0
     "Role"]
    [:div.col-sm-9
     (role :customer)
     (role :dancer)
     (role :staff)]]
   agreement
   control])

(defn page [_]
  (l/frame
   {:nav {:selection nil}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.d-flex.justify-content-center
     (form)]]))

(defn result [{:keys [params]}]
  (let [{:keys [email]} params]
    (l/frame {:nav nil}
                 [:div
                  (if (= "abc@def.com" email)
                    [:p "success"]
                    [:p "fail"])])))

(defn check-nickname [{:keys [params]}]
  (let [{:keys [nickname]} params
        avail? (not (pdb/nickname-existent? nickname))
        valid? (m/validate md/nickname nickname)
        msg (cond
              (and avail? valid?)
              [:p.text-primary
               "This nickname is available to use."]
              (and (not avail?) valid?)
              [:p.text-danger
               "This nickname has already been taken."]
              :else
              [:p.text-danger
               "A nickname cannot have a space or special charaters."])]
    (l/frag [:div msg])))

(defn check-email [{:keys [params]}]
  (let [{:keys [email]} params
        avail? (not (pdb/email-existent? email))
        valid? (m/validate md/email email)
        msg (cond
              (and avail? valid?)
              [:p.text-primary
               "This email is available to use."]
              (and (not avail?) valid?)
              [:p.text-danger
               "This email has already been taken."]
              :else
              [:p.text-danger
               "Must be in valid email format."])]
    (l/frag [:div msg])))

(defn check-password [{:keys [params]}]
  (let [{:keys [password]} params
        valid? (m/validate md/password password)
        msg (if valid?
                [:p.text-primary
                 "Password looks good."]
                [:p.text-danger
                 (str "At least one digit [0-9]"
                      "At least one lowercase character [a-z]"
                      "At least one uppercase character [A-Z]"
                      "At least 8 characters in length, but no more than 32.")])]
    (l/frag [:div msg])))

(def routes
  ["/signup"
   ["" {:get page :post result}]
   ["/check-nickname" {:get check-nickname}]
   ["/check-email" {:get check-email}]
   ["/check-password" {:get check-password}]])
