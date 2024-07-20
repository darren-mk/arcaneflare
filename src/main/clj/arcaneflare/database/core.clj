(ns arcaneflare.database.core
  (:require
   [integrant.core :as ig]
   [next.jdbc :as jdbc]
   [taoensso.timbre :as log]))

(defonce ^:dynamic *ds*
  (atom nil))

(defmethod ig/init-key ::spec
  [_ dbspec]
  (log/info "database spec for postgres set up")
  dbspec)

(defmethod ig/halt-key! ::spec
  [_ _]
  (log/info "database spec for postgres removed"))

(defmethod ig/init-key ::database
  [_ {:keys [dbspec]}]
  (reset! *ds* (jdbc/get-datasource dbspec))
  (log/info "datasource started for postgres"))

(defmethod ig/halt-key! ::database
  [_ _]
  (reset! *ds* nil)
  (log/info "datasource stopped for postgres"))

(defn execute! [ds sql]
  (with-open [conn (jdbc/get-connection ds)]
    (jdbc/execute! conn sql)))

(defn execute-one! [ds sql]
  (with-open [conn (jdbc/get-connection ds)]
    (jdbc/execute-one! conn sql)))
