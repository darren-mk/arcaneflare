(ns arcaneflare.database.place
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hugsql.core :as hugsql]
   [arcaneflare.database.base :as db.base]))

(hugsql/def-db-fns "sql/place.sql")
(declare upsert-place!)
(declare insert-place!)
(declare get-place-by-id)
(declare get-place-by-handle)
(declare get-full-list)
(declare love-place!)
(declare unlove-place!)
(declare get-place-loves)
(declare get-member-loved-places)

(defn insert! [m]
  (insert-place!
   db.base/db m))

(defn upsert! [m]
  (upsert-place!
   db.base/db m))

(defn load-seeds []
  (-> "seeds/places.edn"
      io/resource slurp
      edn/read-string))

(defn upsert-seeds! []
  (doseq [p (load-seeds)]
    (upsert! p)))

(defn single-by [{:keys [id handle]}]
  (cond id (get-place-by-id
            db.base/db {:id id})
        handle (get-place-by-handle
                db.base/db {:handle handle})))

(defn full-list []
  (into [] (get-full-list db.base/db)))

(defn love! [member_id place_id]
  (love-place!
   db.base/db {:member_id member_id
               :place_id place_id}))

(defn unlove! [member_id place_id]
  (love-place!
   db.base/db {:member_id member_id
               :place_id place_id}))

(defn how-loved [place_id]
  (:loves (get-place-loves
           db.base/db {:place_id place_id})))

(comment
  (time
   (upsert-seeds!)))
