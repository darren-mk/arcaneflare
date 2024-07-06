(ns arcaneflare.database.test-core
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [taoensso.timbre :as log]))

(def test-db-spec
  {:dbtype "postgres"
   :dbname "arcaneflare-test"
   :user "dev"
   :password "abc"
   :port 5433})

(def test-mig-config
  {:store :database
   :migration-dir "migrations/"
   :migration-table-name "migrations"
    :port 5433
   :db test-db-spec})

(defn set-test-db! []
  (migratus/migrate
   test-mig-config)
  (log/info "test database set by migration"))

(defn execute! [sql]
  (with-open [conn (-> test-db-spec
                       jdbc/get-datasource
                       jdbc/get-connection)]
    (jdbc/execute! conn sql)))

(defn drop-sql [kind designation]
  [(format "drop %s if exists %s;"
           (name kind)
           (name designation))])

(defn clear-test-db! []
  (let [tables [:people
                :migrations]
        types [:jobs]
        sqls-for-tables (map #(drop-sql :table %)  tables)
        sqls-for-types (map #(drop-sql :type %) types)
        sqls (concat sqls-for-tables sqls-for-types)]
    (doseq [sql sqls]
      (execute! sql))
    (log/info "test database cleared by removing all")))

(defn migration-fixture [f]
  (set-test-db!)
  (f)
  (clear-test-db!))
