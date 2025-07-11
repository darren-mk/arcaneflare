(ns arcaneflare.components.icons)

(defn burger []
  [:svg {:class "w-5 h-5"
    :aria-hidden "true"
    :xmlns "http://www.w3.org/2000/svg"
    :fill "none"
    :viewBox "0 0 17 14"}
   [:path {:stroke "currentColor"
     :stroke-linecap "round"
     :stroke-linejoin "round"
     :stroke-width "2"
     :d "M1 1h15M1 7h15M1 13h15"}]])

(defn arrow-down []
  [:svg {:class "w-2.5 h-2.5 ms-2.5"
         :aria-hidden "true"
         :xmlns "http://www.w3.org/2000/svg"
         :fill "none"
         :viewBox "0 0 10 6"}
   [:path {:stroke "currentColor"
           :stroke-linecap "round"
           :stroke-linejoin "round"
           :stroke-width "2"
           :d "m1 1 4 4 4-4"}]])

(defn close-x []
  [:svg {:class "w-3 h-3"
         :aria-hidden "true"
         :xmlns "http://www.w3.org/2000/svg"
         :fill "none"
         :viewBox "0 0 14 14"}
   [:path {:stroke "currentColor"
           :stroke-linecap "round"
           :stroke-linejoin "round"
           :stroke-width "2"
           :d "m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"}]])