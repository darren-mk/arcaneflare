(ns arcaneflare.database.place.thumbnail
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

(defn add!
  [{:place.thumbnail/keys
    [id place-id image-url alt-text caption position]}]
  (let [q {:insert-into :place-thumbnail
           :columns [:id :place-id :image-url
                     :alt-text :caption :position]
           :values [[id place-id image-url
                     alt-text caption position]]}]
    (db.base/exc (honey.sql/format q))))

(defn get-by
  [{place-id :place/id}]
  (let [q {:select [:*]
           :from [:place-thumbnail]
           :where [:= :place-id place-id]
           :order-by [:position]}]
    (db.base/exc (honey.sql/format q))))

(defn remove!
  [{:place.thumbnail/keys [id]}]
  (let [q {:delete-from :place-thumbnail
           :where [:= :id id]}]
    (db.base/exc (honey.sql/format q))))