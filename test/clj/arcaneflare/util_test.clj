(ns arcaneflare.util-test
  (:require
   [arcaneflare.utils :as sut]
   [clojure.test :as t]
   [orchestra.spec.test :as ost]))

(ost/instrument)

(t/deftest uuid-test
  (t/is (uuid? (sut/uuid))))

(t/deftest sql-dt-test
  (t/is (inst? (sut/sql-dt
                (java.util.Date.)))))

(t/deftest now-test
  (t/is (inst? (sut/now))))

(t/deftest after-days-test
  (t/is (inst? (sut/after-days 3))))

(t/deftest after-minutes-test
  (t/is (inst? (sut/after-minutes 10))))

(t/deftest past?-test
  (t/is (boolean? (sut/past? (sut/now)))))

(t/deftest map->nsmap
  (t/is (= #:yo{:a 1, :b 2}
           (sut/map->nsmap
            {:a 1 :b 2} :yo))))

(t/deftest update-existing-test
  (t/is (= {:a 2} (sut/update-existing {:a 1} :a inc))
        (= {:a 1} (sut/update-existing {:a 1} :b inc))))
