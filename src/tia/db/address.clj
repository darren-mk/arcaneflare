(ns tia.db.address
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn convert [m]
  (let [renaming {:created_at :created-at
                  :edited_at :edited-at}
        address (-> (cset/rename-keys m renaming)
                    (u/map->nsmap :address))]
    (m/coerce model/address address)))

(defn get-all []
  (let [q {:select [:*]
           :from [:address]}]
    (map convert (dbc/hq q))))

(comment
  (get-all)
  :=> '(#:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
                  :street "95 Barclay St",
                  :city "Paterson",
                  :state "NJ",
                  :zip "07503",
                  :country "US",
                  :created-at #inst "2024-03-06T19:14:56.580711000-00:00",
                  :edited-at #inst "2024-03-06T19:14:56.580711000-00:00"}))

(defn create!
  [{:address/keys [id street city state zip country] :as address}]
  (assert (m/validate model/address address))
  (dbc/hd {:insert-into [:addresses]
           :columns [:id :street :city :state :zip :country :created-at :edited-at]
           :values [[id street city state zip country :default :default]]}))

(comment
  (create!
   #:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
             :state "NJ"
             :zip "07503"
             :country "US"
             :street "95 Barclay St"
             :city "Paterson"}))
