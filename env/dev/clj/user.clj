(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [tia.config]
   [clojure.pprint]
   [clojure.string :as cstr]
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [malli.instrument :as mi]
   [mount.core :as mount]
   [tia.core]
   [tia.db.core]
   [tia.db.migration :as mig]))

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

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (inst)
  (mount/start))

(defn stop
  "Stops application."
  []
  (unst)
  (mount/stop))

(defn restart
  "Restarts application."
  []
  (stop)
  (start))

(defn restart-db
  "Restarts database."
  []
  (mount/stop #'tia.db.core/db)
  (mount/start #'tia.db.core/db))

(def migrate! mig/migrate!)

(comment
  (dot "abc def")
  (inst)
  (unst)
  (restart)
  (restart-db)
  (migrate!))
