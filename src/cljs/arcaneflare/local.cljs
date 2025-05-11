(ns arcaneflare.local)

(defn save [k v]
  (.setItem js/localStorage k v))

(defn read [k]
  (.getItem js/localStorage k))

(defn delete [k]
  (.removeItem js/localStorage k))