(ns arcaneflare.component.nav)

(defn node []
  [:div.nav
   [:ul
    [:li [:a {:href "/#/"} "Home"]]
    [:li [:a {:href "/#/place"} "Place"]]
    [:li [:a {:href "/#/review"} "Review"]]
    [:li [:a {:href "/#/discussion"} "Discussion"]]
    [:li [:a {:href "/#/preference"} "Preference"]]
    [:li [:a {:href "/#/login"} "Log in"]]
    [:li [:a {:href "/#/signup"} "Sign up"]]]])