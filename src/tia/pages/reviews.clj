(ns tia.pages.reviews
  (:require
   [tia.calc :refer [>s]]
   [tia.layout :as l]))

(def uri
  "/reviews")

(defn post []
  [:a {:href "#",
       :class (>s :list-group-item :list-group-item-action :d-flex
                  :justify-content-between :align-items-center :py-3)}
   [:div.me-auto
    [:div.fw-bold "Amazing Night at Rick's Cabaret"]
    [:span.text-body-secondary
     (str "jackie028" " at " "11/24/2023 8:39 pm"
          " · " "oharo023" " at " "11/26/2023 2:00 am")]]
   [:div
    [:div "Comments: 23"]
    [:span.text-body-secondary "Views: 280"]]])

(defn listing []
  [:div.list-group
   (for [_ (range 100)]
     (post))])

(defn pagination []
  [:nav {:aria-label "Page navigation example"}
   [:ul.pagination
    [:li {:class "page-item disabled"}
     [:a {:class "page-link"
          :aria-label "Previous"}
      [:span {:aria-hidden "true"}
       [:i {:class "fa-light fa-angle-left"}]]]]
    [:li {:class "page-item active"
          :aria-current "page"}
     [:a {:class "page-link"
          :href "#"} "1"]]
    [:li {:class "page-item"}
     [:a {:class "page-link"
          :href "#"} "2"]]
    [:li {:class "page-item"}
     [:a {:class "page-link"
          :href "#"} "3"]]
    [:li {:class "page-item"}
     [:a {:class "page-link btn-square"
          :href "#"
          :aria-label "Next"}
      [:span {:aria-hidden "true"}
       [:i {:class "fa-light fa-angle-right"
            :aria-hidden true}]]]]]])

(defn page [_req]
  (l/frame {:nav {:selection :reviewlist}}
   [:div.container-md.px-3.px-sm-4.px-xl-5
    [:div.row
     [:div.py-3.py-sm-4
      [:h1.h3.lh-base.mb-1 "Reviews"]
      (pagination)
      (listing)]]]))

(def routes
  [uri {:get page}])
