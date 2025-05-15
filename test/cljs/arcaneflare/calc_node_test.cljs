(ns arcaneflare.calc-node-test
  (:require
   [cljs.test :as t]
   [arcaneflare.calc :as sut]))

(t/deftest routes-dummy-test
  (t/is (= 2 (sut/add 1 1))))

(comment
  (t/run-tests))
