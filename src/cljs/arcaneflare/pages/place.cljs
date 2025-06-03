(ns arcaneflare.pages.place
  (:require
   [reagent.core :as r]
   [arcaneflare.http :refer [tunnel]]))

(defonce loaded
  (r/atom nil))

(defonce modal-opened (r/atom nil))

(def mock-thumbnails
  [{:url "https://placekitten.com/400/300" :alt_text "A cute kitten"}
   {:url "https://placekitten.com/401/300" :alt_text "Another kitten"}
   {:url "https://placekitten.com/402/300" :alt_text "Yet another kitten"}
   {:url "https://placekitten.com/403/300" :alt_text "So many kittens"}])

(def mock-place
  {:name "Velvet Mirage Club"
   :phone_number "(555) 123-4567"
   :website_url "https://velvetmirage.example.com"
   :twitter_url "https://twitter.com/velvetmirage"
   :instagram_url "https://instagram.com/velvetmirage"
   :facebook_url "https://facebook.com/velvetmirage"
   :address "123 Fantasy Lane"
   :city "Neon City"
   :district "Lustre District"
   :state "NV"
   :zipcode "89123"
   :nation "Dreamland"})

(def mock-threads
  [{:id-slug "a1-experience-review"
    :title "My experience at Velvet Mirage"
    :snippet "It was my first visit and I was blown away by the atmosphere..."}
   {:id-slug "a2-best-performers"
    :title "Who are the best performers?"
    :snippet "I'm planning a bachelor party. Who should I look out for?"}
   {:id-slug "a3-food-and-drinks"
    :title "Food and Drinks Menu?"
    :snippet "Do they serve good cocktails? What's the price range like?"}])

(defn thumbnail [thumb]
  [:div.column.is-3
   [:figure.image.is-4by3
    [:img {:src (:url thumb)
           :alt (:alt_text thumb)
           :on-click #(reset! modal-opened (:url thumb))
           :style {:cursor "pointer"}}]]])

(defn thumbnails-section []
  [:section.section
   [:h2.title.is-5 "Gallery"]
   [:div.columns.is-multiline
    (for [thumb mock-thumbnails]
      ^{:key (:url thumb)}
      [thumbnail thumb])]])

(defn modal []
  (when @modal-opened
    [:div.modal.is-active
     [:div.modal-background {:on-click #(reset! modal-opened nil)}]
     [:div.modal-content
      [:p.image
       [:img {:src @modal-opened}]]]
     [:button.modal-close.is-large {:aria-label "close"
                                    :on-click #(reset! modal-opened nil)}]]))

(defn contact-info []
  [:div.box
   [:p [:strong "Phone: "] (:phone_number mock-place)]
   [:p [:strong "Website: "] [:a {:href (:website_url mock-place) :target "_blank"} (:website_url mock-place)]]])

(defn social-links []
  [:div.box
   [:p [:strong "Social"]]
   [:div.buttons
    [:a.button.is-small.is-info {:href (:twitter_url mock-place) :target "_blank"} "Twitter"]
    [:a.button.is-small.is-danger {:href (:instagram_url mock-place) :target "_blank"} "Instagram"]
    [:a.button.is-small.is-link {:href (:facebook_url mock-place) :target "_blank"} "Facebook"]]])

(defn address-box []
  [:div.box
   [:p [:strong "Address:"]]
   [:p (:address mock-place)]
   [:p (str (:district mock-place) ", "
            (:city mock-place) ", "
            (:state mock-place) " "
            (:zipcode mock-place))]
   [:p (:nation mock-place)]])

(defn thread-item [thread]
  [:div.box
   [:a {:href (str "/threads/" (:id-slug thread))}
    [:p.title.is-6 (:title thread)]
    [:p.is-size-7 (:snippet thread)]]])

(defn threads-section []
  [:section.section
   [:h2.title.is-5 "Related Threads"]
   (for [t mock-threads]
     ^{:key (:id-slug t)}
     [thread-item t])])

(defn place-page-mock []
  [:div.container
   [:section.section
    [:h1.title.is-3 (:name mock-place)]
    [:div.columns
     [:div.column.is-two-thirds
      [thumbnails-section]]
     [:div.column.is-one-third
      [contact-info]
      [social-links]
      [address-box]]]]
   [threads-section]
   [modal]])

(defn panel []
  (let [{place-name :place/name
         socials :place/socials} @loaded
       {:keys [website twitter instagram facebook]}
        socials]
    [:div
     [:h2.title.is-5 place-name]
     (when website [:h1 website])
     (when twitter [:h1 twitter])
     (when instagram [:h1 instagram])
     (when facebook [:h1 facebook])]))

(defn node [{:keys [path-params]}]
  (let [{:keys [handle]} path-params]
    (tunnel [:api.public.place/single-by
             {:place/handle handle
              :place/socials? true}]
            #(reset! loaded %)
            #(js/alert %))
    [:div.container
     [panel]]))
