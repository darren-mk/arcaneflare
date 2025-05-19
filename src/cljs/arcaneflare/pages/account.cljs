(ns arcaneflare.pages.account
  (:require
   [reitit.frontend.easy :as rfe]
   [arcaneflare.token :as tk]
   [arcaneflare.state.controlled :as c.state]))

(defn title []
  [:div.level
   [:div.level-left
    [:h1.title "My Account"]]])

(defn logout []
  [:button.button.is-danger.is-light
   {:on-click #(do (println "logging out…")
                   (reset! c.state/member nil)
                   (tk/remove!)
                   (rfe/push-state :page/home))}
   "Log Out"])

(defn profile-section []
  [:div.box
   [:div.level
    [:div.level-left
     [:div.level-item
      [:figure.image.is-128x128
       [:img.is-rounded {:src "/images/profile-placeholder.png"}]]]
     [:div.level-item
      [:div
       [:p.title.is-4 "Darren Kim"]
       [:p.subtitle.is-6 "@darren"]
       [:p "Bio: I like checking out nightlife spots and writing honest reviews."]]]]
    [:div.level-right
     [:div.level-item
      [logout]]]]])

(defn threads-section []
  [:div.box
   [:h2.title.is-5 "My Threads"]
   [:table.table.is-fullwidth.is-striped
    [:thead
     [:tr [:th "Title"] [:th "Date"] [:th "Status"] [:th "Actions"]]]
    [:tbody
     [:tr
      [:td "Review of Club Bliss"]
      [:td "2025-05-10"]
      [:td "Published"]
      [:td
       [:button.button.is-small.is-info "Edit"]
       " "
       [:button.button.is-small.is-danger.is-light "Delete"]]]]]])

(defn replies-section []
  [:div.box
   [:h2.title.is-5 "My Replies"]
   [:ul
    [:li
     [:p "“Totally agree with this review!” on "
      [:a {:href "/reviews/a1b2c3-bliss"} "Club Bliss thread"]
      " (2025-05-09)"]]]])

(defn bookmarks-section []
  [:div.box
   [:h2.title.is-5 "Bookmarks"]
   [:div.columns
    [:div.column
     [:h3.subtitle.is-6 "Clubs"]
     [:ul
      [:li [:a {:href "/clubs/x9y8z7-red-palace"} "Red Palace"]]]]
    [:div.column
     [:h3.subtitle.is-6 "Performers"]
     [:ul
      [:li [:a {:href "/performers/abc123-jade-night"} "Jade Night"]]]]
    [:div.column
     [:h3.subtitle.is-6 "Threads"]
     [:ul
      [:li [:a {:href "/reviews/a1b2c3-bliss"} "Club Bliss Review"]]]]]])

(defn settings-section []
  [:div.box
   [:h2.title.is-5 "Account Settings"]
   [:div.buttons
    [:button.button.is-warning "Change Password"]
    [:button.button.is-light "Notification Preferences"]
    [:button.button.is-danger.is-light "Delete Account"]]])

(defn karma-section []
  [:div.box
   [:h2.title.is-5 "Reputation"]
   [:p "You have earned "
    [:strong "134 karma points"] " from 10 threads and 42 replies."]])

(defn node []
  [:section.section
   [:div.container
    [title]
    [profile-section]
    [threads-section]
    [replies-section]
    [bookmarks-section]
    [settings-section]
    [karma-section]]])