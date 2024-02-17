(ns tia.components.post
  (:require
   [tia.db.place :as db-place]))

(defn industry-selection [given]
  (when-not given
    [:div.input-group.my-3
     [:label.input-group-text
      {:for "example-select-1"}
      "Fun Type"]
     [:select.form-select
      {:id "example-select-1",
       :aria-label "Profession"}
      [:option {:selected ""} "Choose option"]
      [:option {:value :strip-club} "Strip Club"]
      [:option {:value "frontend"} "Front-end engineer"]
      [:option {:value "fullstack"} "Full-stack engineer"]]]))

(def title
  [:div.input-group.my-3
   [:span.input-group-text "Title"]
   [:input.form-control
    {:type :text :name :title
     :placeholder "Write title of your review"
     :aria-label "Products"}]])

(defn place [handle]
  (when-not handle
    [:div.input-group.my-3
     [:span.input-group-text "Place"]
     [:input.form-control
      {:type :text
       :value "Abc"
       :placeholder "Write title of your review"
       :aria-label "Products"}]]))

(def detail
  [:div.input-group.my-3
   [:span.input-group-text
    {:id "add-on-2"} "About"]
   [:textarea.form-control
    {:type :text :name :detail
     :placeholder "Description",
     :rows 10
     :aria-label "Description",
     :aria-describedby "add-on-2"}]])

(def submit
  [:button.btn.btn-primary
   {:type :submit} "Submit"])

(def cancel
  [:button.btn.btn-warning
   "Cancel"])

(defn root [{:keys [handle]}]
  [:form.container.mt-5.px-5
   {:method :post}
   (place handle)
   title
   detail submit cancel])
