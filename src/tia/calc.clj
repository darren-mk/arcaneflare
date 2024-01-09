(ns tia.calc
  "collection of pure functions"
  (:require
   [clojure.string :as cstr]))

(defn >s [& ts]
  (->> ts
       (map name)
       (cstr/join " ")))
