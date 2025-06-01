(ns arcaneflare.database.member.performer-social
  (:require
   [arcaneflare.database.base :as db.base]))

(defn upsert!
  [{performer-member-id :member/id
    platform :performer.social/platform
    url :performer.social/url}]
  (db.base/run
   {:insert-into :performer-social
    :columns [:performer-member-id :platform :url]
    :values [[performer-member-id platform url]]
    :on-conflict [:performer-member-id :platform]
    :do-update-set {:url url}}))

(defn delete!
  [{performer-member-id :member/id
    platform :performer.social/platform}]
  (db.base/run
   {:delete-from :performer-social
    :where [:and
            [:= :performer-member-id
             performer-member-id]
            [:= :platform platform]]}))

(defn all-by
  [{performer-member-id :member/id}]
  (db.base/run
   {:select [:platform :url]
    :from :performer-social
    :where [:= :performer-member-id
            performer-member-id]}))
