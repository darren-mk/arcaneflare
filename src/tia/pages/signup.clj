(ns tia.pages.signup
  (:require
   [clojure.string :as cstr]
   [tia.db.person :as pdb]
   [tia.layout :as l]
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
        existent? (pdb/nickname-existent? nickname)
        tags (if existent?
               [:p "already exists."]
               [:p "good to use"])]
    (l/frag tags)))

(defn check-email [{:keys [params]}]
  (let [{:keys [email]} params]
    (l/frag email)))

(defn check-password [{:keys [params]}]
  (let [{:keys [password]} params]
    (l/frag password)))

(def routes
  ["/signup"
   ["" {:get page :post result}]
   ["/check-nickname" {:get check-nickname}]
   ["/check-email" {:get check-email}]
   ["/check-password" {:get check-password}]])
