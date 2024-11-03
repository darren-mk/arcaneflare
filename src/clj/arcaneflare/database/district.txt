(ns arcaneflare.database.district
  (:require
   [clojure.spec.alpha :as s]
   [arcaneflare.database.interface :as i]))

(defn create! [node {:keys [:district/state-id] :as district}]
  (s/assert :district/object district)
  (assert (i/ent (i/->db node) state-id))
  (i/create-single! node district))

(defn districts-in [db state-id]
  (map first
       (i/query
        db '{:find [(pull ?district [*])]
             :in [[?state-id]]
             :where [[?district :district/state-id ?state-id]]}
        [state-id])))
