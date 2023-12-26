(ns tia.routes
  (:require
   [clojure.string :as cstr]
   [tia.layout :as layout]
   [tia.db.core :as db]
   [tia.middleware :as middleware]
   [ring.util.response]
   [clojure.java.jdbc :as jdbc]))

(defn home [request]
  (layout/render request "home.html"))

(defn database [_request]
  (layout/plain
   (let [ticks (jdbc/with-db-connection [connection {:datasource db/*db*}]
                 (do (jdbc/execute! connection "CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
                     (jdbc/execute! connection "INSERT INTO ticks VALUES (now())")
                     (map :tick (jdbc/query connection "SELECT tick FROM ticks"))))]
     (str "Database Output\n\n" (cstr/join "\n" (map #(str "Read from DB: " %) ticks))))))

(defn ttt [_req]
  (layout/html (str "<p>"
                    _req
                    "</p>")))

(defn routes []
  ["" {:middleware [middleware/wrap-csrf
                    middleware/wrap-formats]}
   ["/" {:get home}]
   ["/ttt" {:get ttt}]
   ["/database" {:get database}]])
