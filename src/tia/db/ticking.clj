(ns tia.db.ticking
  (:require
   [malli.core :as m]
   [tia.db.common :as common]
   [tia.model :as model]
   [tia.util :as u]))

(defn get-all []
  (m/coerce
   [:sequential model/ticking]
   (common/hq
    {:select [:*]
     :from [:ticking]})))

(comment
  (get-all)
  :=> '({:ticking_id #uuid "02a75996-a0e5-4cf3-861e-d459fb10e5f6"
         :created_at #inst "2024-03-10T05:44:08.335000000-00:00"}
        {:ticking_id #uuid "2e51df92-34e6-4c35-98c2-72832516ea4c"
         :created_at #inst "2024-03-10T05:44:11.074000000-00:00"}))

(defn create! []
  (common/hd
   {:insert-into [:ticking]
    :columns [:ticking_id :created_at]
    :values [[(u/uuid) (u/now)]]}))

(comment
  (create!))
