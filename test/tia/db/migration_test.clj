(ns tia.db.migration-test
  (:require
   [clojure.test :as t]
   [tia.db.migration :as src]))

(t/deftest extract-test
  (t/is
   (= #:migration{:time "20240102024754"
                  :id :record-sample-club}
      (src/extract "20240102024754_record-sample-club.edn"))))
