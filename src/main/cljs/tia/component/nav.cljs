(ns tia.component.nav)

(defn node []
  [:div.nav
   [:a {:href "/#/"} "Home"]
   [:a {:href "/#/login"} "Log in"]
   [:a {:href "/#/signup"} "Sign up"]
   [:a {:href "/#/place"} "Place"]
   [:a {:href "/#/review"} "Review"]
   [:a {:href "/#/discussion"} "Discussion"]
   [:a {:href "/#/preference"} "Preference"]])