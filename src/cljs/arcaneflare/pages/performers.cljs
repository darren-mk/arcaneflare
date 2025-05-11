(ns arcaneflare.pages.performers
  (:require
   [clojure.string :as cstr]))

(defn node []
  (let [performers [{:name "Luna Love"
                     :club "Red Velvet"
                     :specialty "Pole Dancing"
                     :rating 4.5}
                    {:name "Cherry Blaze"
                     :club "Neon Nights"
                     :specialty "Freestyle"
                     :rating 3.8}]]

    [:section.section
     [:div.container
      [:h1.title "Featured Performers"]

      [:div.columns.is-multiline
       (for [{:keys [name] :as performer} performers]
         [:div.column.is-full-mobile.is-half-tablet.is-one-third-desktop
          {:key name}
          [:div.card
           [:div.card-image
            [:figure.image.is-4by3
             [:img {:src (:image performer) :alt (:name performer)}]]]

           [:div.card-content
            [:p.title.is-5 (:name performer)]
            [:p.subtitle.is-6 (:club performer)]
            [:p [:strong "Specialty: "] (:specialty performer)]
            [:p [:strong "Rating: "] (str "⭐ " (:rating performer))]]

           [:footer.card-footer
            [:a.card-footer-item.button.is-link.is-fullwidth
             {:href (str "/performers/" (cstr/lower-case
                                         (cstr/replace (:name performer) " " "-")))}
             "View Profile"]]]])]]]))