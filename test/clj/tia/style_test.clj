(ns tia.style-test
  (:require
   [clojure.test :as t]
   [tia.style :as src]))

(t/deftest f-test
  (t/is (= [:.xyz {:color :blue}]
           (apply src/f {:abc/xyz {:color :blue}}))))

(t/deftest c-test
  (t/is (= "xyz fgh"
         (src/c :abc/xyz :def/fgh))))
