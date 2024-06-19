(ns tia.pages.database
  (:require
   #_[clojure.string :as cstr]
   #_[tia.db.tick :as dbt]
   #_[tia.layout :as layout]))

#_
(defn page [_request]
  (dbt/tick!)
  (layout/plain
   (str "Database Output\n\n"
        (cstr/join "\n" (map #(str "Read from DB: " %)
                             (dbt/ticks))))))

#_
(def routes
  ["/database" {:get page}])
