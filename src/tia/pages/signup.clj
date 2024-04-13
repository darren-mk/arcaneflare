(ns tia.pages.signup
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.db.person :as db-person]
   [tia.layout :as l]
   [tia.model :as md]
   [tia.calc :refer [>s]]
   [tia.util :as u]))

(defn fail [k]
  (let [msg (-> k name (cstr/replace #"-" " "))]
    [:div [:h3.text-bg-danger msg]]))

(comment
  (fail :abc-def)
  :=> [:div [:h3.text-bg-danger "abc def"]])

(defn success []
  [:div
   [:h3.text-bg-primary
    "Sign up successful. You can now log in."]
   [:a {:href "/login"}
    "Log in"]])

(defn submit [{:keys [params] :as _req}]
  (let [{:keys [nickname email password
                job agreed?]} params
        id (u/uuid)
        person #:person{:id id
                        :nickname nickname
                        :email email
                        :password password
                        :job (keyword job)
                        :verified? (= "on" agreed?)
                        :created-at (u/now)
                        :edited-at (u/now)}]
    (try (db-person/create! person)
         (catch Exception _))
    (l/elem
     (if (db-person/get-by-id id)
       (success)
       (fail :error-recording-in-db)))))

(defn input [k]
  (let [target (str (name k) "check-result")
        label (-> k name cstr/capitalize)
        checker {:hx-get (str "/signup/validate-" (name k))
                 :hx-trigger (>s :input :changed)
                 :hx-indicator :.htmx-indicator
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

(defn job [k]
  [:div.form-check
   [:input.form-check-input
    {:name :job :type :radio
     :required true :value k}]
   [:label.form-check-label
    (-> k name cstr/capitalize)]])

(def agreement
  [:div.row.mb-3
   [:div.col-sm-9.ms-auto
    [:div.form-check
     [:input.form-check-input
      {:type :checkbox 
       :required true
       :value false
       :name :agreed?}]
     [:label.form-check-label
      {:for "agree-to-terms-2"}
      "I agree to the"
      [:a {:href "#"}
       "Terms &amp; Conditions"]]]]])

(def control
  [:div.text-end
   [:a.btn.btn-secondary
    {:href "/"} "Cancel"]
   [:button.btn.btn-primary
    {:type :submit} "Sign up"]])

(def have-account
  [:div.text-center.text-body-secondary
   "Already have account?"
   [:a {:href "/login"} "Log In"]])

(def form
  [:form {:hx-post "/signup/submit"}
   (input :nickname)
   (input :email)
   (input :password)
   [:fieldset.row.mb-3
    [:legend.col-form-label.mb-1.mb-sm-0.col-sm-3.pt-0
     "Role"]
    [:div.col-sm-9
     (job :customer)
     (job :dancer)
     (job :staff)]]
   agreement control have-account])

(defn root-page [_]
  (l/page
   {:nav {:selection nil}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.d-flex.justify-content-center
     form]]))

(defn validate-nickname [{:keys [params]}]
  (let [{:keys [nickname]} params
        avail? (not (db-person/nickname-exists? nickname))
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
    (l/elem [:div msg])))

(defn validate-email [{:keys [params]}]
  (let [{:keys [email]} params
        unavailable? (db-person/email-exists? email)
        invalid? (not (m/validate md/email email))
        msg (cond
              unavailable?
              [:p.text-danger "This email has already been taken."]
              invalid?
              [:p.text-danger "Must be in valid email format."]
              :else
              [:p.text-primary "This email is available to use."])]
    (l/elem [:div msg])))

(defn validate-password [{:keys [params]}]
  (let [{:keys [password]} params
        valid? (m/validate md/password password)
        msg (if valid?
              [:p.text-primary
               "Password looks good."]
              [:div
               [:p.text-danger "Password must contain one digit from 1 to 9,"]
               [:p.text-danger "one lowercase letter,"]
               [:p.text-danger "one uppercase letter,"]
               [:p.text-danger "one special character, no space,"]
               [:p.text-danger "and it must be 8-16 characters long."]])]
    (l/elem [:div msg])))

(def routes
  ["/signup"
   ["" {:get root-page}]
   ["/validate-nickname" {:get validate-nickname}]
   ["/validate-email" {:get validate-email}]
   ["/validate-password" {:get validate-password}]
   ["/submit" {:post submit}]])
