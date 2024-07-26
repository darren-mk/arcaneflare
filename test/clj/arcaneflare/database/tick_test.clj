(ns clj.arcaneflare.database.tick-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.interface :as i]
   [arcaneflare.database.tick :as sut]))

(t/deftest integrate-test
  (with-open [node (i/->node {})]
    (i/atx node (sut/tick! node))
    (i/atx node (sut/tick! node))
    (i/atx node (sut/tick! node))
    (let [db (i/->db node)
          ticks (sut/ticks db)]
      (t/is (= 3 (count ticks))))))
