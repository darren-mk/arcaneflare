(ns tia.pages.login
  (:require
   [clojure.string :as cstr]
   [tia.layout :as layout]))

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

(defn form []
  [:form {:action "/login"
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
     (form)]]))

(defn result [{:keys [params]}]
  (let [{:keys [email password]} params]
    (layout/frame {:nav nil}
                 [:div
                  (if (= "abc@def.com" email)
                    [:p "success"]
                    [:p "fail"])])))

(def routes
  ["/login" {:get page
             :post result}])
