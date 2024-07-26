(ns arcaneflare.database.interface-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [orchestra.spec.test :as ost]
   [arcaneflare.database.interface :as sut]))

(s/check-asserts true)
(ost/instrument 'sut)

(def id-a 1)

(def id-b 2)

(def val-a "a")

(def val-b "b")

(def m-a
  {:xt/id id-a
   :my/value val-a})

(def m-a-alt
  {:xt/id id-a
   :my/value val-b})

(def m-b
  {:xt/id id-b
   :my/value val-b})

(defn qr [v]
  {:find '[(pull ?e [*])]
   :where [['?e :my/value v]]})

(t/deftest ->id-test
  (t/is (= 1 (sut/->id
              {:xt/id 1
               :my/value "a"}))))

(t/deftest create-single-ql-test
  (t/is (= [[:xtdb.api/match 1 nil]
            [:xtdb.api/put m-a]]
           (sut/create-single-ql m-a))))

(t/deftest create-single!-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-single! node m-a))
    (sut/atx node (sut/create-single!
                   node (assoc m-a :my/value "z")))
    (let [db (sut/->db node)]
      (t/is (= 1 (count (sut/history db 1))))
      (t/is (= m-a (sut/ent db id-a))))))

(t/deftest create-multi-ql-test
  (t/is (= [[:xtdb.api/match 1 nil]
            [:xtdb.api/put {:xt/id 1, :my/value "a"}]
            [:xtdb.api/match 2 nil]
            [:xtdb.api/put {:xt/id 2, :my/value "b"}]]
           (sut/create-multi-ql [m-a m-b]))))

(t/deftest create-multi!-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-multi!
                   node [m-a m-b]))
    (let [db (sut/->db node)]
      (t/is (= 2 (sut/count-by-k db :xt/id)))
      (t/is (= m-a (ffirst (sut/query db (qr val-a))))))))

(t/deftest update-single-ql-test
  (t/is (= [[:xtdb.api/match id-a m-a]
            [:xtdb.api/put m-a-alt]]
           (sut/update-single-ql m-a m-a-alt))))

(t/deftest update-single!-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-single! node m-a))
    (sut/atx node (sut/update-single! node m-a m-a-alt))
    (let [db (sut/->db node)]
      (t/is (= 2 (count (sut/history db id-a))))
      (t/is (= m-a-alt (sut/ent db id-a))))))

(t/deftest history-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-single! node m-a))
    (sut/atx node (sut/update-single! node m-a m-a-alt))
    (sut/atx node (sut/update-single! node m-a-alt m-a))
    (let [db (sut/->db node)
          created-at (sut/created-at db 1)
          last-edited-at (sut/last-edited-at db 1)]
      (t/is (= 3 (count (sut/history db 1))))
      (t/is (inst? created-at))
      (t/is (inst? last-edited-at))
      (t/is (= m-a (sut/ent db id-a))))))

(t/deftest pull-by-k-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-multi!
                   node [m-a m-b]))
    (let [db (sut/->db node)]
      (t/is (= 2 (sut/count-by-k db :xt/id)))
      (t/is (= [m-b m-a] (sut/pull-by-k db :my/value))))))

(t/deftest pull-by-kv-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-multi!
                   node [m-a m-b]))
    (let [db (sut/->db node)]
      (t/is (= 2 (sut/count-by-k db :xt/id)))
      (t/is (= [m-b] (sut/pull-by-kv db :my/value "b"))))))

(t/deftest delete!-test
  (with-open [node (sut/->node {})]
    (t/is node)
    (sut/atx node (sut/create-single! node m-a))
    (sut/atx node (sut/update-single! node m-a m-a-alt))
    (sut/atx node (sut/delete! node id-a))
    (let [db (sut/->db node)
          created-at (sut/created-at db 1)
          last-edited-at (sut/last-edited-at db 1)]
      (t/is (= 3 (count (sut/history db 1))))
      (t/is (inst? created-at))
      (t/is (inst? last-edited-at))
      (t/is (= nil (sut/ent db id-a))))))
