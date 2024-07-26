(ns arcaneflare.database.core
  (:require
   [clojure.java.io :as io]
   [mount.core :as m]
   [taoensso.timbre :as log]
   [arcaneflare.database.interface :as i]))

(declare node)

(m/defstate ^:dynamic node
  :start
  (if-let [db-spec {:host "localhost"
                    :dbname "arcaneflare"
                    :user "dev"
                    :password "abc"}]
    (do (log/info "DB configs are successfully loaded.")
        (i/->node
         {:xtdb.jdbc/connection-pool
          {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
           :db-spec db-spec}
          :xtdb/tx-log
          {:xtdb/module 'xtdb.jdbc/->tx-log
           :connection-pool :xtdb.jdbc/connection-pool}
          :xtdb/document-store
          {:xtdb/module 'xtdb.jdbc/->document-store
           :connection-pool :xtdb.jdbc/connection-pool}
          :xtdb/index-store
          {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                      :db-dir (io/file "/tmp/rocksdb")}}}))
    (do (log/warn "database connection URL was not found")
        node))
  :stop
  (.close node))
