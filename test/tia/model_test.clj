(ns tia.model-test
  (:require
   [clojure.test :as t]
   [malli.core :as m]
   [tia.model :as model]))

(t/deftest nickname-test
  (let [f #(m/validate model/nickname %)]
    (t/testing "valid cases"
      (t/are [s] (f s)
        "abc123" "123abc" "123Abc" "123aBC"))
    (t/testing "invalid cases"
      (t/are [s] (not (f s))
        "abc 123" "abc@123" "!abc123"
        "a b c 1 2 3" "Abc1@3"
        "abc" "abcdefghijk123456789"))))

(t/deftest email-test
  (let [f #(m/validate model/email %)]
    (t/testing "valid cases"
      (t/are [s] (f s)
        "abc@def.com"))
    (t/testing "invalid cases"
      (t/are [s] (not (f s))
        "abc@123" "abc@d ef.com"))))

(t/deftest password-test
  (let [f #(m/validate model/password %)]
    (t/testing "valid cases"
      (t/are [s] (f s)
        "Abc123xyz@"))
    (t/testing "invalid cases"
      (t/are [s] (not (f s))
        "abc12345678" "123" "abc"))))
