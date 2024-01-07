(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [tia.config]
   [clojure.pprint]
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [malli.instrument :as mi]
   [mount.core :as mount]
   [tia.core]
   [tia.db.core]))

(alter-var-root
 #'s/*explain-out*
 (constantly expound/printer))

(add-tap
 (bound-fn* clojure.pprint/pprint))

(defn inst []
  (mi/instrument!))

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start))

(defn stop
  "Stops application."
  []
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

(comment
  (inst)
  (restart)
  (restart-db))
