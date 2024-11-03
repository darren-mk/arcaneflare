(ns user
  (:require
   [clojure.tools.namespace.repl :as tn]
   [mount.core :as m]
   [clojure.spec.alpha :as s]
   [orchestra.spec.test :as ost]
   [arcaneflare.core :as c]
   [arcaneflare.database.core :as dbc]))

(defn start []
  (s/check-asserts true)
  (ost/instrument)
  (m/start #'dbc/conn
           #'c/server))

(defn stop []
  (s/check-asserts false)
  (ost/unstrument)
  (m/stop))

(defn restart []
  (tn/refresh-all)
  (stop)
  (start))

(comment
  (start)
  (stop)
  (restart))
