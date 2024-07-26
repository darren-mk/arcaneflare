(ns arcaneflare.database.api-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.api :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def mini-m
  {:xt/id 1 :data/char "a"})

(def ql
  '{:find [(pull ?e [*])]
    :where [[?e :data/char "a"]]})

(t/deftest integrate-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/put! node mini-m))
    (sut/atx node (sut/put! node (assoc mini-m :data/char "z")))
    (sut/atx node (sut/put! node mini-m))
    (let [db (sut/->db node)
          created-at (sut/created-at db 1)
          last-edited-at (sut/last-edited-at db 1)]
      (t/is (inst? created-at))
      (t/is (inst? last-edited-at))
      (t/is (= mini-m (ffirst (sut/query db ql)))))))
