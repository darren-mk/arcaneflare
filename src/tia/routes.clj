(ns tia.routes
  (:require
   [clojure.string :as cstr]
   [tia.style :as style]
   [tia.layout :as layout]
   [tia.db.core :as db]
   [tia.pages.landing :as landing]
   [tia.middleware :as middleware]
   [ring.util.response]
   [clojure.java.jdbc :as jdbc]))

(defn home [request]
  (layout/render request "home.html"))

(defn database [_request]
  (db/tick!)
  (layout/plain
   (str "Database Output\n\n"
        (cstr/join "\n" (map #(str "Read from DB: " %)
                             (db/ticks))))))

(defn routes []
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/" {:get home}]
   ["/ttt" {:get landing/ttt}]
   ["/css" {:get style/core}]
   ["/database" {:get database}]])
