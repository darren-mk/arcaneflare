(ns arcaneflare.database.place.love
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

(defn yes!
  [{member-id :member/id place-id :place/id}]
  (let [q {:insert-into :place-love
           :columns [:member-id :place-id]
           :values [[member-id place-id]]
           :on-conflict [:member-id :place-id]
           :do-nothing true}]
    (db.base/exc (honey.sql/format q))))

(defn no!
  [{member-id :member/id place-id :place/id}]
  (let [q {:delete-from :place-love
           :where [:and
                   [:= :member-id member-id]
                   [:= :place-id place-id]]}]
    (db.base/exc (honey.sql/format q))))

(defn how
  [{place-id :place/id}]
  (let [q {:select [[[:raw "count(*)"]]]
           :from [:place-love]
           :where [:= :place-id place-id]}]
    (-> q honey.sql/format
        db.base/exc first :count)))

(defn by-member
  [{member-id :member/id}]
  (let [q {:select [[:place-love.place-id]]
           :from [:place-love]
           :where [:= :place-love.member-id member-id]}]
    (db.base/exc (honey.sql/format q))))