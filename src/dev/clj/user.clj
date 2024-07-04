(ns user 
  (:require
   [integrant.repl :as ir]
   [clojure.spec.alpha :as s]
   [arcaneflare.core :as tc]
   [arcaneflare.database.migrate :as dm]
   [taoensso.timbre :as log]))

(ir/set-prep! 
 (constantly tc/config))

(def create-mg
  dm/create!)

(def migrate!
  dm/migrate!)

(def rollback!
  dm/rollback!)

(defn spec-assert [b]
  (let [msg (str "spec assertion is "
                 (if b "on" "off"))]
    (s/check-asserts true)
    (log/info msg)))

(defn start []
  (spec-assert true)
  (ir/go))

(def stop ir/halt)

(comment
  (create-mg :create-people-table)
  (migrate!)
  (rollback!)
  (start)
  (stop))

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
  (mount/stop #'arcaneflare.db.core/db)
  (mount/start #'arcaneflare.db.core/db))

(def migrate! mig/migrate!)

(comment
  (dot "abc def")
  (inst)
  (unst)
  (restart)
  (restart-db)
  (migrate!))

