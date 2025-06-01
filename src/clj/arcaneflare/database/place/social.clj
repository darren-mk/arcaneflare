(ns arcaneflare.database.place.social
  (:require
   [arcaneflare.database.base :as db.base]))

(defn upsert!
  [{place-id :place/id
    platform :place.social/platform
    url :place.social/url}]
  (db.base/run
   {:insert-into :place_social
    :columns [:place_id :platform :url]
    :values [[place-id (name platform) url]]
    :on-conflict [:place_id :platform]
    :do-update-set {:url url}}))

(defn delete!
  [{place-id :place/id
    platform :place.social/platform}]
  (db.base/run
   {:delete-from :place_social
    :where [:and
            [:= :place_id place-id]
            [:= :platform platform]]}))

(defn all-by
  [{place-id :place/id}]
  (db.base/run
   {:select [:platform :url]
    :from :place_social
    :where [:= :place_id place-id]}))
