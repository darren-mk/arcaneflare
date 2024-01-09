(ns tia.util
  "utility functions that involve side effects
  or that are not regarding domain logic."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [malli.core :as m]))

(defn uuid []
  (java.util.UUID/randomUUID))

(defn now []
  (java.util.Date.))

(defn read-time-s [s]
  (let [frm "yyyyMMddhhmmss"
        jdf (java.text.SimpleDateFormat. frm)]
    (.parse jdf s)))

(defn file-names! [dir]
  (let [obj (io/file dir)
        names (map #(.getName %) (file-seq obj))
        pred #(= ".edn" (cstr/join (take-last 4 %)))]
    (filter pred names)))

(m/=> read-time-s
      [:=> [:cat :string]
       inst?])
