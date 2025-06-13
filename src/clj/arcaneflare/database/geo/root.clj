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

(defn countries [_]
  (base/run
   {:select [:*]
    :from :geo
    :where [:= :kind "country"]}))

(defn children-by [{id :geo/id}]
  (base/run
    {:select [:*]
     :from :geo
     :where [:= :parent-id id]}))

(defn endings-by
  [{:geo/keys [id kind full-name]}]
  (let [where (if id [:= :id id]
                  [:and
                   [:= :kind kind]
                   [:= :full-name full-name]])
        q {:with-recursive
           [[:descendants
             {:union-all [{:select [:*] :from [:geo]
                           :where where}
                          {:select [:g.*] :from [[:geo :g]]
                           :join [[:descendants :d]
                                  [:= :g.parent-id :d.id]]}]}]]
           :select [:id :full-name]
           :from [:descendants]
           :where [:= :is-ending true]}]
    (base/run q)))
