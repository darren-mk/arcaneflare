(ns tia.style-test
  (:require
   [clojure.test :as t]
   [tia.style :as src]))

(t/deftest fmt-test
  (t/is (= [:.xyz {:color :blue}]
           (apply src/fmt {:abc/xyz {:color :blue}}))))

(t/deftest cl-test
  (with-redefs [src/sum #:css{:abc :blue
                              :def :red}]
    (t/is (= "abc def"
             (src/cl :css/abc :css/def)))))
