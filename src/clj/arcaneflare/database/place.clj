(ns arcaneflare.database.place
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hugsql.core :as hugsql]
   [arcaneflare.database.base :as db.base]))

(hugsql/def-db-fns "sql/place.sql")
(declare insert-place!)
(declare upsert-place!)
(declare get-place-by-id)
(declare get-place-by-handle)

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

(defn get-single-by [{:keys [id handle]}]
  (cond id (get-place-by-id
            db.base/db {:id id})
        handle (get-place-by-handle
                db.base/db {:handle handle})))

(comment
  (upsert!
   {:id #uuid "d5f1c09d-7459-4cf4-bb7e-d20de74ac089"
    :name "Sapphire Las Vegas"
    :handle "d5f1c09d-sapphire-las-vegas"
    :address "3025 Sammy Davis Jr Dr, Las Vegas, NV 89109"
    :city "Las Vegas"
    :district "The Strip"
    :state "NV"
    :zipcode "89109"
    :country "USA"
    :county "Clark"
    :region "Southwest"
    :lat 36.1335
    :lon -115.1797}))

(comment
  (upsert-seeds!))

(comment
  (get-single-by
   {:id #uuid "d5f1c09d-7459-4cf4-bb7e-d20de74ac089"})
  (get-single-by
   {:handle "d5f1c09d-sapphire-las-vegas"})
  (get-place-by-id
   db.base/db {:id #uuid "d5f1c09d-7459-4cf4-bb7e-d20de74ac089"}))