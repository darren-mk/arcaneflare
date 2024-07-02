(ns arcaneflare.database.core
  (:require
   [integrant.core :as ig]
   [next.jdbc :as jdbc]
   [hugsql.core :as hugsql]
   [hugsql.adapter.next-jdbc :as adapter]
   [taoensso.timbre :as log]))

(def ds
  (atom nil))

(defmethod ig/init-key ::spec
  [_ dbspec]
  (log/info "database spec for postgres set up")
  dbspec)

(defmethod ig/halt-key! ::spec
  [_ _]
  (log/info "database spec for postgres removed"))

(def sql-files
  #{"arcaneflare/database/sql/tekadon.sql"})

(defn register-sql [files]
  (let [adapter (adapter/hugsql-adapter-next-jdbc)]
    (doseq [file files]
      (hugsql/def-db-fns
        file {:adapter adapter}))))

(defmethod ig/init-key ::database
  [_ {:keys [dbspec]}]
  (reset! ds (jdbc/get-datasource dbspec))
  (register-sql sql-files)
  (log/info "datasource started for postgres"))

(defmethod ig/halt-key! ::database
  [_ _]
  (reset! ds nil)
  (log/info "datasource stopped for postgres"))
