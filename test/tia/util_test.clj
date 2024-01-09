(ns tia.util-test
  (:require
   [clojure.test :as t]
   [tia.util :as src]))

(t/deftest uuid-test
  (t/is (uuid? (src/uuid))))

(t/deftest now-test
  (t/is (inst? (src/now))))

(t/deftest read-timetag-test
  (t/is (= #inst "2024-01-02T07:47:54.000-00:00"
           (src/read-time-s "20240102024754"))))

(t/deftest file-names!-test
  (let [mig-file-names (src/file-names!
                        "resources/migrations")]
    (t/is (coll? mig-file-names))
    (t/is (not-empty mig-file-names))))
