(ns arcaneflare.database.tick.root
  (:require
   [arcaneflare.database.base :as base]))

(defn now [_]
  (-> ["select now()"]
      base/exc
      first :now))

(comment
  (now {}))