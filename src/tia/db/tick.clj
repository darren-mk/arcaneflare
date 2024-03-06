(ns tia.db.tick
  (:require
   [malli.core :as m]
   [tia.db.common :as common]
   [tia.model :as model]))

(defn convert
  [{:keys [id created_at]}]
  (let [tick #:tick{:id id
                    :created-at created_at}]
    (m/coerce model/tick tick)))

(defn get-all []
  (->> {:select [:*]
        :from [:ticks]}
       common/hq
       (map convert)))

(comment
  (get-all)
  :=> (#:tick{:id #uuid "f8b00820-8d90-4441-bdb9-6048d0171be9"
              :created-at #inst "2024-03-06T18:24:06.255017000-00:00"}
       #:tick{:id #uuid "cc49b830-28d5-4cb0-93b5-e8fd8272959e"
              :created-at #inst "2024-03-06T18:26:24.385753000-00:00"}))

(defn create! []
  (common/hd
   {:insert-into [:ticks]
    :columns [:id :created-at]
    :values [[:default :default]]}))

(comment
  (create!))
