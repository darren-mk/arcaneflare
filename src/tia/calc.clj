(ns tia.calc
  "pure functions for domain logic."
  (:require
   [clojure.string :as cstr]))

(defn >s [& ts]
  (->> ts
       (map name)
       (cstr/join " ")))

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
