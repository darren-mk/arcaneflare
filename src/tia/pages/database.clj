(ns tia.pages.database
  (:require
   [tia.db.tick :as db-tick]
   [tia.layout :as layout]))

(defn page [_]
  (db-tick/create!)
  (layout/plain
   (map #(str (:tick/created-at %) "\n")
        (db-tick/get-all))))

(def routes
  ["/database" {:get page}])
