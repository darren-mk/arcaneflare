(ns arcaneflare.database.migrate
  (:require
   [clojure.set :as cset]
   [integrant.core :as ig]
   [taoensso.timbre :as log]
   [migratus.core :as migratus]))

(def config
  (atom nil))

(defn create! [phrase]
  (migratus/create
   {:migration-dir "migrations"}
   (name phrase)))

(comment
  (create! :create-foo))

(defn rollback! []
  (if @config
    (migratus/rollback @config)
    (log/warn "migration config not set up")))

(comment
  (rollback!)
  :=> nil)

(defn migrate! []
  (if @config
    (migratus/migrate @config)
    (log/warn "migration config not set up")))

(comment
  (migrate!)
  :=> nil)

(defmethod ig/init-key ::migration
  [_ opts]
  (reset! config (cset/rename-keys opts {:dbspec :db}))
  (migrate!)
  (log/info "database migrated to latest"))

(defmethod ig/halt-key! ::migration
  [_ _]
  (reset! config nil)
  (log/info "database migration unmounted"))

(comment
  (migratus/migrate
   {:store :database
    :migration-dir "migrations/"
    :migration-table-name "migrations"
    :db {:dbtype "postgres"
         :dbname "arcaneflare"
         :user "dev"
         :password "abc"}}))
