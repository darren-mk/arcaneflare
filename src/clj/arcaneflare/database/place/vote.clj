(ns arcaneflare.database.place.vote
  (:require
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]))

(defn make!
  [{member-id :member/id place-id :place/id
    up-or-down :member/up-or-down}]
  (let [score (case up-or-down
                :up 1 :down -1)
        q {:insert-into :place-vote
           :columns [:member-id :place-id :score]
           :values [[member-id place-id score]]
           :on-conflict [:member-id :place-id]
           :do-update-set {:score :excluded.score
                           :voted-at [:raw "now()"]}}]
    (db.base/exc (honey.sql/format q))))

(defn remove!
  [{member-id :member/id place-id :place/id}]
  (let [q {:delete-from :place-vote
           :where [:and
                   [:= :member-id member-id]
                   [:= :place-id place-id]]}]
    (db.base/exc (honey.sql/format q))))

(defn by-place
  [{place-id :place/id}]
  (let [q {:select [[[:raw "sum(score)"] :score]]
           :from [:place-vote]
           :where [:= :place-id place-id]}]
    (-> q honey.sql/format db.base/exc
        first :score)))