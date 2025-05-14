(ns arcaneflare.pages.account
  (:require
   [arcaneflare.state.controlled :as state]))

(defn node []
  (let [{:user/keys [name description]}
        @state/user]
    [:section.section
     [:div.container

      ;; Heading
      [:div.level
       [:div.level-left
        [:h1.title "My Account"]]]

      ;; Profile section
      [:div.box
       [:div.columns
        [:div.column.is-one-quarter
         [:figure.image.is-128x128
          [:img.is-rounded {:src "/images/profile-placeholder.png"}]]]

        [:div.column
         [:p.title.is-4 name]
         [:p.subtitle.is-6 description]
         [:p description]
         [:button.button.is-small.is-light {:on-click #(js/alert "Edit profile")} "Edit Profile"]]]]

      ;; My Threads
      [:div.box
       [:h2.title.is-5 "My Threads"]
       [:table.table.is-fullwidth.is-striped
        [:thead
         [:tr [:th "Title"] [:th "Date"] [:th "Status"] [:th "Actions"]]]
        [:tbody
         ;; Replace below with loop over user's threads
         [:tr
          [:td "Review of Club Bliss"]
          [:td "2025-05-10"]
          [:td "Published"]
          [:td
           [:button.button.is-small.is-info "Edit"]
           " "
           [:button.button.is-small.is-danger.is-light "Delete"]]]]]]

      ;; Replies
      [:div.box
       [:h2.title.is-5 "My Replies"]
       [:ul
        [:li
         [:p "“Totally agree with this review!” on "
          [:a {:href "/reviews/a1b2c3-bliss"} "Club Bliss thread"] " (2025-05-09)"]]
        ;; more replies...
        ]]

      ;; Saved Items
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
          [:li [:a {:href "/reviews/a1b2c3-bliss"} "Club Bliss Review"]]]]]]

      ;; Settings
      [:div.box
       [:h2.title.is-5 "Account Settings"]
       [:div.buttons
        [:button.button.is-warning "Change Password"]
        [:button.button.is-light "Notification Preferences"]
        [:button.button.is-danger.is-light "Delete Account"]]]

      ;; Optional: Reputation / Karma
      [:div.box
       [:h2.title.is-5 "Reputation"]
       [:p "You have earned "
        [:strong "134 karma points"] " from 10 threads and 42 replies."]]]]))