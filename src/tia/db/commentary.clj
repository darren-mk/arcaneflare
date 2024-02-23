(ns tia.db.commentary
  (:require
   [tia.db.common :as common]))

(defn get-commentaries-by-post-id [post-id]
  (let [qr '{:find [(pull ?commentary [*])]
             :in [[?post-id]]
             :where [[?commentary :commentary/post-id ?post-id]
                     [?commentary :commentary/updated ?updated]]}
        raw (common/query qr [post-id])]
    (->> raw
         (map first)
         (sort-by :commentary/updated))))
