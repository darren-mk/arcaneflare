(ns tia.db.post
  (:require
   [tia.db.common :as common]))

(defn get-posts-by-place [pid]
  (common/query
   '{:find [(pull ?post [*])]
     :in [[?pid]]
     :where [[?post :post/place-id ?pid]]}
   [pid]))

(defn get-by-handle [handle kind]
  (let [qr '{:find [(pull ?post [*])]
             :in [[?handle ?kind]]
             :where [[?place :place/handle ?handle]
                     [?place :place/id ?place-id]
                     [?post :post/place-id ?place-id]
                     [?post :post/literature ?kind]]}]
    (map first (common/query qr [handle kind]))))

(comment
  (get-by-handle :silk-gentlemens-club :review))
