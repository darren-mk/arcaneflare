(ns tia.model-test
  (:require
   [clojure.test :as t]
   [malli.core :as m]
   [tia.model :as model]))

(t/deftest tick-test
  (t/is
   (m/validate
    model/tick
    {:tick/id #uuid "f1d8bc86-b0d9-439d-b3e8-06d113bbc076"
     :tick/timestamp #inst "2024-01-29T15:14:31.246-00:00"})))

(t/deftest coordinate-test
  (t/is
   (m/validate
    model/coordinate
    {:latitude 40.8539175
     :longitude -74.1404719})))

(t/deftest address-test
  (t/is
   (m/explain
    model/address
    {:address/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
     :address/language :en
     :address/googleid "ChIJ0XE9giD_wokREL4wlGyI9cg"
     :address/googleuri "https://maps.google.com/?cid=14480630176803765776"
     :address/coordinate {:latitude 40.8539175
                          :longitude -74.1404719}
     :address/number "40"
     :address/street "95 Barclay St"
     :address/city "Paterson"
     :address/county "Bergen"
     :address/state :nj
     :address/zip "07503"
     :address/country :usa})))

(t/deftest place-test
  (t/is
   (m/validate
    model/place
    {:place/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"
     :place/language :en
     :place/industry :club
     :place/label "Johnny A’s Hitching Post"
     :place/handle :johnny-as-hitching-post
     :place/status :operational
     :place/paymethods #{:cash :credit}
     :place/nudity :none
     :place/website "http://johnnyashitchingpost.com/"
     :place/facebook "https://www.facebook.com/JohnnyAsHitchingPost"
     :place/twitterx "https://twitter.com/hitchingpostnj"
     :place/instagram "https://www.instagram.com/Hitching_Post_/"
     :place/phone "1-973-684-7678"
     :place/schedules ["mon 4pm - 10pm" "tue 4pm - 11pm"]
     :place/address-id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"})))

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
