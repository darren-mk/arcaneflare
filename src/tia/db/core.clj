(ns tia.db.core
  (:require
   [next.jdbc.date-time]
   [next.jdbc.prepare]
   [next.jdbc.result-set]
   [clojure.tools.logging :as log]
   [tia.config :as config]
   [tia.model]
   [mount.core :refer [defstate]]
   [xtdb.api :as xt]))

(declare db)

(defstate ^:dynamic db
  :start
  (if-let [db-spec {:host (:db-host config/env)
                    :dbname (:db-name config/env)
                    :user (:db-user config/env)
                    :password (:db-password config/env)}]
    (do (log/info "DB configs are successfully loaded.")
        (xt/start-node
         {:xtdb.jdbc/connection-pool
          {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
           :db-spec db-spec}
          :xtdb/tx-log
          {:xtdb/module 'xtdb.jdbc/->tx-log
           :connection-pool :xtdb.jdbc/connection-pool}
          :xtdb/document-store
          {:xtdb/module 'xtdb.jdbc/->document-store
           :connection-pool :xtdb.jdbc/connection-pool}}))
    (do (log/warn "database connection URL was not found")
        db))
  :stop
  (.close db))
