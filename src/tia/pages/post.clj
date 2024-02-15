(ns tia.pages.post
  (:require
   [tia.calc :refer [>s]]
   [tia.data :as d]
   [tia.layout :as l]))

(def industry
  [:div.input-group.my-3
   [:label.input-group-text
    {:for "example-select-1"}
    "Fun Type"]
   [:select.form-select
    {:id "example-select-1",
     :aria-label "Profession"}
    [:option {:selected ""} "Choose option"]
    [:option {:value "backend"} "Back-end engineer"]
    [:option {:value "frontend"} "Front-end engineer"]
    [:option {:value "fullstack"} "Full-stack engineer"]]])

(def title
  [:div.input-group.my-3
   [:span.input-group-text "Title"]
   [:input.form-control
    {:type :text
     :placeholder "Write title of your review"
     :aria-label "Products"}]])

(def place
  [:div.input-group.my-3
   [:span.input-group-text "Place"]
   [:input.form-control
    {:type :text
     :value "Abc"
     :placeholder "Write title of your review"
     :aria-label "Products"}]])

(def detail
  [:div.input-group.my-3
   [:span.input-group-text
    {:id "add-on-2"} "About"]
   [:textarea.form-control
    {:placeholder "Description",
     :rows 10
     :aria-label "Description",
     :aria-describedby "add-on-2"}]])

(defn page [_]
  (l/frame
   {:nav {:selection nil}}
   [:div.container.mt-5.px-5
    industry place title detail
    [:button {:type "submit", :class "btn btn-primary"} "Submit"]]))

(def routes
  [(:post d/uri)
   ["" {:get page}]])
