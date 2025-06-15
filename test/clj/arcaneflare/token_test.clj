(ns arcaneflare.token-test
  (:require
   [clojure.test :as t]
   [arcaneflare.token :as a]))

(t/deftest gen!-test
  (t/is (string? (a/gen! {:id "abc123"}))))

(t/deftest verify-test
  (let [token (a/gen! {:id "abc123"
                       :role :performer})
        {:keys [id role iat exp]}
        (a/unsign token)]
    (t/is (and id iat exp
               (= "abc123" id)
               (= role "performer")))))
