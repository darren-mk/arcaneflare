(ns tia.db.address
  (:require
   [malli.core :as m]
   [tia.calc :as c]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn coerce [m]
  (-> m c/kebab-m
      (u/map->nsmap :address)))
(u/mf coerce [:map model/address])

(defn get-all []
  (let [q {:select [:*]
           :from [:address]}]
    (map coerce (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
                  :street "95 Barclay St",
                  :city "Paterson",
                  :state "NJ",
                  :zip "07503",
                  :country "US",
                  :created-at #inst "2024-03-23T18:58:01.694072000-00:00",
                  :edited-at #inst "2024-03-23T18:58:01.694072000-00:00"}))

(defn create!
  [{:address/keys [id street city state zip country] :as address}]
  (assert (m/validate model/address address))
  (dbc/hd {:insert-into [:address]
           :columns [:id :street :city :state :zip :country :created-at :edited-at]
           :values [[id street city state zip country :default :default]]}))

(comment
  (create!
   #:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
             :state "NJ"
             :zip "07503"
             :country "US"
             :street "95 Barclay St"
             :city "Paterson"})
  :=> nil)

(defn find-address-by-handle [handle]
  (let [qr {:select [:address.*]
            :from [:address]
            :join [:place [:= :address.id :place.address_id]]
            :where [[:= :place.handle (name handle)]]}]
    (-> qr dbc/hq first coerce)))

(comment
  (find-address-by-handle :johnny-as-hitching-post))

(defn find-cities-in-state [state]
  (let [qr {:select [:address.city]
            :from [:address]
            :where [[:= :address.state state]]}]
    (->> qr dbc/hq
         (map :city))))

(comment
  (find-cities-in-state "NJ")
  :=> '("Paterson"))
