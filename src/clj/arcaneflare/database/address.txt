(ns arcaneflare.database.address
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(defn create! [node {:keys [:address/city-id] :as address}]
  (s/assert :address/object address)
  (assert (i/ent (i/->db node) city-id))
  (i/create-single! node address))

(defn addresses-in [db city-id]
  (map first
       (i/query
        db '{:find [(pull ?address [*])]
             :in [[?city-id]]
             :where [[?address :address/city-id ?city-id]]}
        [city-id])))
