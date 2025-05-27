(ns arcaneflare.database.base-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.base :as a]))

(t/deftest connection-test
  (t/is (= [{:abc 1}]
           (a/run {:select [[:1 :abc]]}))))