(ns arcaneflare.database.tick
  (:require
   [arcaneflare.database.interface :as i]
   [arcaneflare.util :as u]))

(defn tick! [node]
  (let [tick {:xt/id (u/uuid)
              :tick/timestamp (u/now)}]
    (i/create-single! node tick)))

(defn ticks [db]
  (let [ql '{:find [timestamp]
             :where [[?tick :tick/timestamp timestamp]]
             :order-by [[timestamp :asc]]}]
    (mapv first (i/query db ql))))
