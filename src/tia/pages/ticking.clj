(ns tia.pages.ticking
  (:require
   [tia.db.ticking :as db-ticking]
   [tia.layout :as layout]))

(defn page [_]
  (db-ticking/create!)
  (layout/plain
   (map #(str (:created_at %) "\n")
        (db-ticking/get-all))))

(def routes
  ["/ticking" {:get page}])
