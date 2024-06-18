(ns tia.pages.database
  (:require
   [clojure.string :as cstr]
   [tia.db.tick :as dbt]
   [tia.layout :as layout]))

(defn page [_request]
  (dbt/tick!)
  (layout/plain
   (str "Database Output\n\n"
        (cstr/join "\n" (map #(str "Read from DB: " %)
                             (dbt/ticks))))))

(def routes
  ["/database" {:get page}])
