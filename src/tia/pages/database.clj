(ns tia.pages.database
  (:require
   [clojure.string :as cstr]
   [tia.db.tick :as db-tick]
   [tia.layout :as layout]))

(defn page [_]
  (db-tick/create!)
  (layout/plain
   (cstr/join
    "\n"
    (str (db-tick/get-all)))))

(def routes
  ["/database" {:get page}])
