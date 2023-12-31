(ns tia.db.core
  (:require
   [next.jdbc.date-time]
   [next.jdbc.prepare]
   [next.jdbc.result-set]
   [clojure.tools.logging :as log]
   [tia.config :refer [env]]
   [tia.util :as u]
   [mount.core :refer [defstate]]
   [xtdb.api :as xt]))

(declare *db*)

(defstate ^:dynamic *db*
  :start
  (if-let [jdbc-url (env :database-url)]
    (do (log/info "DB configs are successfully loaded.")
        (xt/start-node
         {:xtdb.jdbc/connection-pool
          {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
           :db-spec {:jdbcUrl jdbc-url}}
          :xtdb/tx-log
          {:xtdb/module 'xtdb.jdbc/->tx-log
           :connection-pool :xtdb.jdbc/connection-pool}
          :xtdb/document-store
          {:xtdb/module 'xtdb.jdbc/->document-store
           :connection-pool :xtdb.jdbc/connection-pool}}))
    (do (log/warn "database connection URL was not found")
        *db*))
  :stop
  (.close *db*))

(defn tick! []
  (let [id (u/uuid)
        m {:xt/id id
           :tick/id id
           :tick/timestamp (u/now)}]
    (xt/submit-tx *db*
     [[::xt/put m]])))

(defn ticks []
  (let [q (xt/q
            (xt/db *db*)
            '{:find [timestamp]
              :where [[?tick :tick/timestamp timestamp]]
              :order-by [[timestamp :asc]]})]
   (mapv first q)))
