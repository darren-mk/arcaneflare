(ns tia.db.ticking
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.db.common :as common]
   [tia.model :as model]
   [tia.util :as u]))

(defn translate [m]
  (let [renaming {:created_at :created-at}
        ticking (-> (cset/rename-keys m renaming)
                    (u/map->nsmap :ticking))]
    (m/coerce model/ticking ticking)))

(defn get-all []
  (let [q {:select [:*]
           :from [:ticking]}]
    (map translate (common/hq q))))

(comment
  (take 2 (get-all))
  :=> '(#:ticking{:id #uuid "02a75996-a0e5-4cf3-861e-d459fb10e5f6",
                  :created-at #inst "2024-03-10T05:44:08.335000000-00:00"}
        #:ticking{:id #uuid "2e51df92-34e6-4c35-98c2-72832516ea4c",
                  :created-at #inst "2024-03-10T05:44:11.074000000-00:00"}))

(defn create! []
  (common/hd
   {:insert-into [:ticking]
    :columns [:id :created-at]
    :values [[(u/uuid) (u/now)]]}))

(comment
  (create!))
