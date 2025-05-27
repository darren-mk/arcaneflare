(ns arcaneflare.database.member.performer
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]
   [arcaneflare.utils :as u]))

(defn upsert!
  [{member-id :member/id
    display-name :performer/display-name
    bio :performer/bio}]
  (let [q {:insert-into :performer
           :values [[member-id display-name bio (u/now)]]
           :columns [:member-id :display-name :bio :edited-at]
           :on-conflict [:member_id]
           :do-update-set {:display-name :excluded.display-name
                           :bio :excluded.bio}}]
    (db.base/exc (sql/format q))))

(defn remove!
  [{member-id :member/id}]
  (let [q {:delete-from :performer
           :where [:= :member_id member-id]}]
    (db.base/exc (sql/format q))))

(defn get-by
  [{member-id :member/id}]
  (let [q {:select [:*]
           :from [:performer]
           :where [:= :member_id member-id]}]
    (first (db.base/exc (sql/format q)))))