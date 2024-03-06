(ns tia.db.address
  (:require
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.model :as model]))

(defn get-all []
  (dbc/hq
   {:select [:*]
    :from [:addresses]}))

(comment
  (get-all)
  :=> '({:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
         :street "95 Barclay St"
         :city "Paterson"
         :state "NJ"
         :zip "07503"
         :country "US"
         :created_at #inst "2024-03-06T19:14:56.580711000-00:00"
         :updated_at #inst "2024-03-06T19:14:56.580711000-00:00"}))

(defn create!
  [{:address/keys [id street city state zip country] :as address}]
  (assert (m/validate model/address address))
  (dbc/hd {:insert-into [:addresses]
           :columns [:id :street :city :state :zip :country :created-at :updated-at]
           :values [[id street city state zip country :default :default]]}))

(comment
  (create!
   #:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
             :state "NJ"
             :zip "07503"
             :country "US"
             :street "95 Barclay St"
             :city "Paterson"}))
