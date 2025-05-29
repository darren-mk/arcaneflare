(ns arcaneflare.pages.place
  (:require
   [reagent.core :as r]
   [arcaneflare.state.loaded :as loaded-state]
   [arcaneflare.http :refer [tunnel]]))

(defonce modal-opened
  (r/atom nil))

(defn thumbnail [thumb]
  [:div.column.is-3
   [:figure.image.is-4by3
    [:img {:src (:url thumb)
           :alt (:alt_text thumb)
           :on-click #(reset! modal-opened (:url thumb))
           :style {:cursor "pointer"}}]]])

(defn thumbnails-section [thumbnails]
  [:section.section
   [:h2.title.is-5 "Gallery"]
   [:div.columns.is-multiline
    (for [thumb thumbnails]
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

(defn contact-info [{:keys [phone_number website_url]}]
  [:div.box
   [:p [:strong "Phone: "] phone_number]
   (when website_url
     [:p [:strong "Website: "] [:a {:href website_url :target "_blank"} website_url]])])

(defn social-links [{:keys [twitter_url instagram_url facebook_url]}]
  [:div.box
   [:p [:strong "Social"]]
   [:div.buttons
    (when twitter_url
      [:a.button.is-small.is-info {:href twitter_url :target "_blank"} "Twitter"])
    (when instagram_url
      [:a.button.is-small.is-danger {:href instagram_url :target "_blank"} "Instagram"])
    (when facebook_url
      [:a.button.is-small.is-link {:href facebook_url :target "_blank"} "Facebook"])]])

(defn address-box [{:keys [address city district state zipcode country]}]
  [:div.box
   [:p [:strong "Address:"]]
   [:p address]
   [:p (str district ", " city ", " state " " zipcode)]
   [:p country]])

(defn thread-item [thread]
  [:div.box
   [:a {:href (str "/threads/" (:id-slug thread))}
    [:p.title.is-6 (:title thread)]
    [:p.is-size-7 (:snippet thread)]]])

(defn threads-section [threads]
  [:section.section
   [:h2.title.is-5 "Related Threads"]
   (for [t threads]
     ^{:key (:id t)}
     [thread-item t])])

(defn place-page
  [{:keys [place thumbnails threads]}]
  [:div.container
   [:section.section
    [:h1.title.is-3 (:name place)]
    [:div.columns
     #_[:div.column.is-two-thirds
      [thumbnails-section thumbnails]]
     [:div.column.is-one-third
      [contact-info place]
      [social-links place]
      [address-box place]]]]
   #_[threads-section threads]
   #_[modal]])

(defn node [{:keys [path-params]}]
  (let [{:keys [handle]} path-params]
    (when-not (get @loaded-state/places handle)
      (tunnel [:api.public.place/single-by
               {:place/handle handle}]
              #(swap! loaded-state/places
                      assoc handle %)
              #(js/alert %)))
    [place-page {:place (get @loaded-state/places handle)}]))
