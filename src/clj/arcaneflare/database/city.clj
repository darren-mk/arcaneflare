(ns arcaneflare.database.city
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(defn create! [node {:keys [:city/district-id] :as city}]
  (s/assert :city/object city)
  (assert (i/ent (i/->db node) district-id))
  (i/create-single! node city))

(defn cities-in [db district-id]
  (map first
       (i/query
        db '{:find [(pull ?city [*])]
             :in [[?district-id]]
             :where [[?city :city/district-id ?district-id]]}
        [district-id])))
