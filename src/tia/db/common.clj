(ns tia.db.common
  (:require
   [clojure.java.jdbc :as jdbc]
   [honey.sql :as sql]
   [tia.db.core :as db-core]))

(defn q [s]
  (jdbc/with-db-connection
    [conn {:datasource db-core/db}]
    (jdbc/query conn s)))

(defn hq [code]
  (-> code sql/format q))

(def h sql/format)

(defn d [& codes]
  (jdbc/with-db-connection
    [conn {:datasource db-core/db}]
    (doseq [code codes]
      (jdbc/execute! conn code))))

(defn hd [code]
  (-> code sql/format d))

(def ->jsonb
  db-core/clj->jsonb-pgobj)

(comment
  (->jsonb {:a 1 :b "2"}))

(def ->edn
  db-core/pgobj->clj)

(comment
  (let [obj (->jsonb {:a 1 :b "2"})]
    (->edn obj))
  :=> {:a 1, :b "2"})
