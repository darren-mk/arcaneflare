(ns tia.util-test
  (:require
   [clojure.test :as t]
   [tia.util :as src]))

(t/deftest uuid-test
  (t/is (uuid? (src/uuid))))

(t/deftest now-test
  (t/is (inst? (src/now))))
