(ns arcaneflare.core-test
  (:require
   [cljs.test :as t]
   [arcaneflare.core :as sut]))

(t/deftest routes-dummy-test
  (t/is (vector? sut/routes)))

(comment
  (cljs.test/run-tests))
