(ns arcaneflare.database.country
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(defn create! [node country]
  (s/assert :country/object country)
  (i/create-single! node country))
