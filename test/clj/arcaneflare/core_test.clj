(ns arcaneflare.core-test
  (:require
   [clojure.test :as t]
   [arcaneflare.core :as a]
   [arcaneflare.token :as tk]))

(t/deftest api-test
  (t/testing "check member id and role are included"
    (let [k :api.private.test/mock
          args {:abc/def 123}
          member-m #:member{:id 789 :role "staff"}]
      (with-redefs [a/fnk-map {k identity}
                    tk/verify (constantly member-m)]
        (t/is (= (merge args member-m)
                 (a/api k args "faketoken")))))))