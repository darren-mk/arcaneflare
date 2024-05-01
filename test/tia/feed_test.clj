(ns tia.feed-test
  (:require
   [clojure.test :as t]
   [tia.feed :as src]))

(t/deftest parse-address-test
  (t/testing "address without unit"
    (t/is
     (= #:address{:id #uuid "77d50617-ff07-4342-b7e3-c52bfcae707d"
                  :street "7 Chome-14 Roppongi" :city "Minato City"
                  :state "Tokyo" :zip "106-0032" :country "Japan"}
        (src/parse-address #uuid "77d50617-ff07-4342-b7e3-c52bfcae707d"
                           "7 Chome-14 Roppongi, Minato City, Tokyo 106-0032, Japan"))))
  (t/testing "address with unit"
    (t/is
     (= #:address{:id #uuid "77d50617-ff07-4342-b7e3-c52bfcae707d"
                  :street "Unit 103, 7 Chome-14 Roppongi" :city "Minato City"
                  :state "Tokyo" :zip "106-0032" :country "Japan"}
        (src/parse-address #uuid "77d50617-ff07-4342-b7e3-c52bfcae707d"
                           "Unit 103, 7 Chome-14 Roppongi, Minato City, Tokyo 106-0032, Japan")))))
