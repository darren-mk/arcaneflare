(ns tia.calc-test
  (:require
   [clojure.test :as t]
   [tia.calc :as src]))

(t/deftest >s-test
  (t/is (= "abc def"
           (src/>s :abc :def)))
  (t/is (= "keyup changed delay:500ms"
           (src/>s :keyup :changed :delay:500ms))))

(t/deftest nsmap->ns-test
  (t/is
   (= "abc"
      (src/nsmap->ns
       {:xt/id 7 :abc/x 1 :abc/y 2}))))

(t/deftest ns->idk-test
  (t/is
   (= :abc/id
      (src/ns->idk "abc"))))

(t/deftest nsmap->idk-test
  (t/is
   (= :abc/id
      (src/nsmap->idk
       {:abc/x 1 :abc/y 2}))))

(t/deftest ns->schema-test
  (t/is (vector? (src/ns->schema "club"))))
