(ns tia.pages.login
  (:require
   [clojure.string :as cstr]
   [tia.layout :as layout]
   [tia.db.session :as session-db]))

(defn result [{:keys [params]}]
  (let [m (select-keys params [:email :password])
        session (session-db/login! m)
        prop (if session
               {:session {:id (:session/id session)}}
               {})
        tag (if session
              [:div [:p "logged in"]]
              [:div [:p "Failed to log in."]])]
    (layout/frame prop tag)))

(defn input [k]
  (let [label (-> k name
                  cstr/capitalize)]
    [:div.mb-3
     [:label.form-label
      {:for :username}
      label]
     [:input.form-control
      {:name k :type k
       :required true
       :placeholder label}]]))

(def sign-in
  [:div.mb-3.pb-3.border-bottom
   [:button.btn.btn-primary.w-100
    {:type :submit} "Sign in"]])

(def no-account
  [:div.text-center.text-body-secondary
   "Don't have an account?"
   [:a {:href "/signup"} "Sign up"]])

(def form
  [:form {:action "/login/result"
          :method :post}
   (input :email)
   (input :password)
   sign-in
   no-account])

(defn page [_]
  (layout/frame
   {:nav {:selection nil}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.d-flex.justify-content-center
     form]]))

(def routes
  ["/login"
   ["" {:get page}]
   ["/result" {:post result}]])
