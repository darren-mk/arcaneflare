(ns arcaneflare.database.geo.root
  (:require
   [arcaneflare.database.base :as base]))

(defn upsert!
  [{:geo/keys [id kind full-name parent-id is-ending]}]
  (let [q {:insert-into :geo
           :columns [:id :kind :full-name :parent-id :is-ending]
           :values [[id kind full-name parent-id is-ending]]
           :on-conflict [:id]
           :do-update-set {:id :excluded.id
                           :kind :excluded.kind
                           :full-name :excluded.full-name
                           :parent-id :excluded.parent-id
                           :is-ending :excluded.is-ending}}]
    (base/run q)))

(defn seed! []
  (doseq [{:keys [id kind full-name parent-id is-ending]}
          (base/bring-csv "resources/seeds/geos.csv")]
    (upsert!
     #:geo{:id (parse-uuid id)
           :kind kind
           :full-name full-name
           :parent-id (parse-uuid parent-id)
           :is-ending (= "true" is-ending)})))

(defn multi-by
  [{id :geo/id
    kind :geo/kind
    fraction :geo/fraction
    parent-id :geo/parent-id}]
  (let [wrap #(str "%" % "%")
        filters [(when id [:= :id id])
                 (when kind [:= :kind kind])
                 (when fraction [:ilike :full-name (wrap fraction)])
                 (when parent-id [:= :parent-id parent-id])]
        where (into [:and] (remove nil? filters))
        q (merge {:select [:*]
                  :from :geo}
                 (when (seq where) {:where where}))]
    (base/run q)))
