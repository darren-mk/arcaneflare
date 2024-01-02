(ns tia.pages.clubs.individual
  (:require
   [tia.db.club :as db-club]
   [tia.layout :as layout]))

(defn page [req]
  (let [handle (-> req :path-params :handle keyword)
        data (db-club/find-club-by-handle handle)
        tags [:p (-> data :club :club/instagram)]]
    (->> tags (cons :div) vec layout/html)))

(def routes
  ["/clubs/individual/:handle" {:get page}])
