(ns tia.pages.database
  (:require
   [clojure.string :as cstr]
   [tia.db.core :as db]
   [tia.layout :as layout]))

(defn page [_request]
  (db/tick!)
  (layout/plain
   (str "Database Output\n\n"
        (cstr/join "\n" (map #(str "Read from DB: " %)
                             (db/ticks))))))

(def routes
  ["/database" {:get page}])
