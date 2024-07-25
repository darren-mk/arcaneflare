(ns user
  (:require
   [mount.core :as m]
   [clojure.spec.alpha :as s]
   [orchestra.spec.test :as ost]))

(defn start []
  (s/check-asserts true)
  (ost/instrument)
  (m/start))

(defn stop []
  (s/check-asserts false)
  (ost/unstrument)
  (m/stop))

(defn restart []
  (stop)
  (start))

(comment
  (start)
  (stop)
  (restart))

