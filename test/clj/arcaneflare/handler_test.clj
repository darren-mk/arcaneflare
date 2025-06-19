(ns arcaneflare.handler-test
  (:require
   [clojure.test :as t]
   [arcaneflare.handler :as a]
   [arcaneflare.token :as tk]))

(t/deftest public-api?-test
  (t/testing "public samples"
    (t/are [k] (true? (a/public-api? k))
      :api.public.test/mock))
  (t/testing "private samples"
    (t/are [k] (false? (a/public-api? k))
      :api.private.test/mock)))

(t/deftest api-test
  (t/testing "private apis"
    (let [fnk :api.private.test/mock
          id (random-uuid)
          role "staff"
          token (tk/gen! {:member/id id
                          :member/role role})
          args {:abc/def 123
                :member/token token}]
      (with-redefs [a/fnk-map {fnk identity}]
        (t/is (= {:abc/def 123
                  :member/id id
                  :member/role role}
                 (a/api fnk args)))))))