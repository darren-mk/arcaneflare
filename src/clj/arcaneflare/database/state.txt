(ns arcaneflare.database.state
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(defn create! [node {:keys [:state/country-id] :as state}]
  (s/assert :state/object state)
  (assert (i/ent (i/->db node) country-id))
  (i/create-single! node state))

(defn states-in [db country-id]
  (map first
       (i/query
        db '{:find [(pull ?state [*])]
             :in [[?country-id]]
             :where [[?state :state/country-id ?country-id]]}
        [country-id])))
