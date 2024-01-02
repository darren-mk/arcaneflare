(ns tia.pages.clubs.state
  (:require
   [tia.db.club :as db-club]
   [tia.layout :as layout]))

(defn page [req]
  (let [state-key (-> req :path-params :state keyword)
        data (mapv (fn [item] [:a {:href (str "/clubs/individual/" (-> item :handle name))}
                               (-> item :label)])
                   (db-club/find-clubs-by-state state-key))]
    (->> data (cons :div) vec layout/html)))

(def routes
  ["/clubs/country/:country/state/:state" {:get page}])
