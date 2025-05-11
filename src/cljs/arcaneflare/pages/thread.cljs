(ns arcaneflare.pages.thread)

(defn node [{:keys [path-params]}]
  (let [{:keys [_handle]} path-params
        thread {:title "Red Velvet Review"
                :author "Alex"
                :club "Red Velvet"
                :rating 4.5
                :content "The vibe was great, and dancers were friendly. Drinks were strong and reasonably priced."
                :tags ["VIP" "Chill" "Couples"]}
        replies [{:user "Jamie" :content "Totally agree. Loved the vibe."}
                 {:user "Casey" :content "Had a different experience — was too loud for me."}]]

    [:section.section
     [:div.container

      ;; === Back Button
      [:div.mb-4
       [:a.button.is-light {:href "/#/threads"}
        [:span.icon [:i.fas.fa-arrow-left]]
        [:span "Back to Threads"]]]

      ;; === Thread Header ===
      [:h1.title (:title thread)]
      [:p.subtitle
       "Posted by " [:strong (:author thread)]
       " — ⭐ " (:rating thread)]

      ;; === Club & Tags (optional)
      [:div.mb-4
       [:p "Club: " [:strong (:club thread)]]
       [:div.tags
        (for [tag (:tags thread)]
          [:span.tag.is-info tag])]]

      ;; === Content ===
      [:div.content
       [:p (:content thread)]]

      [:hr]

      ;; === Replies ===
      [:h2.title.is-5 "Replies"]
      (if (empty? replies)
        [:p.has-text-grey "No replies yet."]
        (for [{:keys [user content]} replies]
          [:div.box
           [:p [:strong user]]
           [:p content]]))

      ;; === Reply Form ===
      [:div.mt-5
       [:h3.title.is-6 "Leave a Reply"]
       [:div.field
        [:div.control
         [:textarea.textarea {:placeholder "Write your reply..."}]]]
       [:div.field.mt-2
        [:div.control
         [:button.button.is-link "Post Reply"]]]]]]))