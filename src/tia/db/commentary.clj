(ns tia.db.commentary
  (:require
   [tia.db.common :as common]))

(defn get-by-post-id [post-id]
  (map first
       (common/query
        '{:find [(pull ?commentary [*])]
          :in [[?post-id]]
          :where [[?commentary :commentary/post-id ?post-id]]}
        [post-id])))
