(ns arcaneflare.database.core
  (:require
   [integrant.core :as ig]
   [next.jdbc :as jdbc]
   [hugsql.core :as hugsql]
   [hugsql.adapter.next-jdbc :as adapter]
   [taoensso.timbre :as log]))

(def ds
  (atom nil))

(def sql-files
  #{"arcaneflare/database/sql/tekadon.sql"})

(defn register-sql [files]
  (let [adapter (adapter/hugsql-adapter-next-jdbc)]
    (doseq [file files]
      (hugsql/def-db-fns
        file {:adapter adapter}))))

(defmethod ig/init-key ::database
  [_ spec]
  (log/info "datasource started for postgres")
  (reset! ds (jdbc/get-datasource spec))
  (register-sql sql-files))

(defmethod ig/halt-key! ::database
  [_ _]
  (log/info "datasource stopped for postgres")
  (reset! ds nil))

(comment
  (declare testeron)
  (testeron @ds)
  :=> [{:?column? 123}])


