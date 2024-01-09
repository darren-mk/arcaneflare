(ns tia.calc-test
  (:require
   [clojure.test :as t]
   [tia.calc :as src]))

(t/deftest >s-test
  (t/is
   (= "abc def"
      (src/>s :abc :def))))
