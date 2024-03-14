(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [clojure.pprint]
   [clojure.string :as cstr]
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [luminus-migrations.core :as migrations]
   [malli.instrument :as mi]
   [mount.core :as mount]
   [tia.config :as config]
   [tia.core]
   [tia.db.core]))

(alter-var-root
 #'s/*explain-out*
 (constantly expound/printer))

(add-tap
 (bound-fn* clojure.pprint/pprint))

(defn dot [s]
  (as-> s $
    (cstr/split $ #" ")
    (cstr/join "." $)))

(defn inst []
  (mi/instrument!))

(defn unst []
  (mi/unstrument!))

(defn start []
  (inst)
  (mount/start))

(defn stop []
  (unst)
  (mount/stop))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'tia.db.core/db)
  (mount/start #'tia.db.core/db))

(defn migrate! []
  (let [km (select-keys config/env [:database-url])]
    (migrations/migrate ["migrate"] km)))

(defn schemafy! [s]
  (let [km (select-keys config/env [:database-url])]
    (migrations/create s km)))

(comment
  (dot "abc def")
  (inst)
  (unst)
  (restart)
  (restart-db)
  (migrate!)
  (schemafy! "rename address edited at field"))
