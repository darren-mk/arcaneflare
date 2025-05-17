(ns arcaneflare.database.base
  (:require
   [next.jdbc :as jdbc]
   [arcaneflare.env :as env]))

(def db
  (get (env/config) :pg))

(def ds
  (jdbc/get-datasource db))

(comment
  (jdbc/execute!
   ds ["SELECT current_database(), current_user, version();"]))