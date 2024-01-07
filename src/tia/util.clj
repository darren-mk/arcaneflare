(ns tia.util
  "utility functions that
  involve side effects."
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

(defn nsmap->ns [m]
  (-> (dissoc m :xt/id)
      keys first namespace))

(def ns->idk
  #(keyword (str % "/id")))

(def nsmap->idk
  (comp ns->idk nsmap->ns))

(def ns->schema
  #(->> % (str "tia.model/")
        symbol eval))

(defn file-names! [dir]
  (let [obj (io/file dir)
        names (map #(.getName %) (file-seq obj))
        pred #(= ".edn" (cstr/join (take-last 4 %)))]
    (filter pred names)))

(m/=> read-time-s
      [:=> [:cat :string]
       inst?])
