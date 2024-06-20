(ns user 
  (:require
   [integrant.repl :as ir]
   [tia.core :as tc]))

(ir/set-prep! (constantly tc/config))

(def start! ir/go)

(def stop! ir/halt)

(comment
  (start!)
  (stop!))

#_#_#_#_#_#_#_#_#_#_#_
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
  [] (stop)
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

