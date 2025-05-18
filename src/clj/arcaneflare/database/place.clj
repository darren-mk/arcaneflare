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
  (vector (get-full-list db.base/db)))

(comment
  (time
   (upsert-seeds!)))

(comment
  (single-by
   {:id #uuid "d5f1c09d-7459-4cf4-bb7e-d20de74ac089"})
  (single-by
   {:handle "d5f1c09d-sapphire-las-vegas"})
  (get-place-by-id
   db.base/db {:id #uuid "d5f1c09d-7459-4cf4-bb7e-d20de74ac089"}))