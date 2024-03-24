(ns tia.pages.landing
  (:require
   [tia.layout :as l]
   [tia.style :as s]))

(defn page [{:keys [session person]}]
  (l/page {:nav {:selection nil}
           :session session :person person}
          [:main {:class "position-relative flex-grow-1 d-flex align-items-center"}
           [:div {:class "container-fluid g-0 overflow-hidden position-relative z-1"}
            [:h1 {:class (s/cl :css/big-font)} "hello"]]]))

(def routes
  ["/" {:get page}])
