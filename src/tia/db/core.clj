(ns tia.db.core
  (:require
   [next.jdbc.date-time]
   [next.jdbc.prepare]
   [next.jdbc.result-set]
   [clojure.edn :as cedn]
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [clojure.tools.logging :as log]
   [tia.config :as config]
   [tia.model]
   [tia.util :as u]
   [malli.core :as m]
   [mount.core :refer [defstate]]
   [xtdb.api :as xt]))

(declare *db*)

(defstate ^:dynamic *db*
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
        *db*))
  :stop
  (.close *db*))

(defn query
  ([ql]
   (xt/q (xt/db *db*) ql))
  ([ql var]
   (xt/q (xt/db *db*) ql var)))

(comment
  (query
   '{:find [?migration]
     :in [[migration-label]]
     :where [[?migration :migration/label migration-label]]}
   [:record-sample-club])
  :=> #{[#uuid "bb008ec8-daf2-4abd-a9f9-49d9ff553cd8"]})

(defn record! [data]
  (let [ns-s (-> data keys first namespace)
        id-k (keyword (str ns-s "/id"))
        schema (-> (str "tia.model/" ns-s) symbol eval)
        id-v (get data id-k)
        m (assoc data :xt/id id-v)]
    (if (m/validate schema m)
      (xt/submit-tx *db* [[::xt/put m]])
      (log/error "data not validated."))))

(defn delete! [id]
  (xt/submit-tx *db*
   [[::xt/delete id]]))

(defn tick! []
  (record! {:tick/id (u/uuid)
            :tick/timestamp (u/now)}))

(comment
  (tick!)
  :=> #:xtdb.api{:tx-id 222
                 :tx-time #inst "2024-01-02T05:38:04.708-00:00"})

(defn ticks []
  (let [q (xt/q
           (xt/db *db*)
           '{:find [timestamp]
             :where [[?tick :tick/timestamp timestamp]]
             :order-by [[timestamp :asc]]})]
    (mapv first q)))

(comment
  (xt/pull
   (xt/db *db*)
   '{:find [(pull [*])]
     :where [[?tick :tick/id]]}))

(defn migrate-file! [filename]
  (let [ms (->> filename (str "migrations/")
                io/resource slurp cedn/read-string)
        mg-label (-> ms first :migration/label)
        exists? (boolean (ffirst (xt/q (xt/db *db*)
                   '{:find [?migration]
                     :in [[migration-label]]
                     :where [[?migration :migration/label migration-label]]}
                   [mg-label])))]
    (if exists?
      "already migrated."
      (doseq [m ms]
        (record! m)))))

(defn migration-exists? [id]
  (let [read (query
              '{:find [id]
                :in [[id]]
                :where [[?mig :migration/id id]]}
              [id])]
    (-> read ffirst boolean)))

(defn migrate! []
  (let [dir (io/file "resources/migrations")
      read (map #(.getName %) (file-seq dir))
      pred #(= ".edn" (cstr/join (take-last 4 %)))
      files (filter pred read)]
  (doseq [file files]
    (if (migration-exists? file)
      (log/info file "has already been migrated.")
      (do (migrate-file! file)
          (log/info file "is just migrated."))))))
