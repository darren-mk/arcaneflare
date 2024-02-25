(ns tia.pages.landing
  (:require
   [tia.layout :as l]))

(defn page [_req]
  (l/page {:nav {:selection nil}}
   [:main {:class "position-relative flex-grow-1 d-flex align-items-center"}
    [:div {:class "container-fluid g-0 overflow-hidden position-relative z-1"}
     [:h1 "hello"]]]))

(def routes
  ["/" {:get page}])
