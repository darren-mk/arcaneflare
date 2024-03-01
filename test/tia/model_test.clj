(ns tia.model-test
  (:require
   [clojure.test :as t]
   [malli.core :as m]
   [tia.model :as model]))

(def sample-tick
  #:tick{:id #uuid "f1d8bc86-b0d9-439d-b3e8-06d113bbc076"
         :timestamp #inst "2024-01-29T15:14:31.246-00:00"})

(def sample-address
  #:address{:id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"
            :state "NJ"
            :language :en
            :zip "07503"
            :country :usa
            :street "95 Barclay St"
            :city "Paterson"
            :county "Bergen"})

(def sample-place
  #:place{:phone "1-973-684-7678",
          :address-id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
          :facebook "https://www.facebook.com/JohnnyAsHitchingPost",
          :language :en,
          :status :operational,
          :id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4",
          :website "http://johnnyashitchingpost.com/",
          :twitterx "https://twitter.com/hitchingpostnj",
          :nudity :none,
          :label "Johnny A’s Hitching Post",
          :instagram "https://www.instagram.com/Hitching_Post_/",
          :industry :strip-club,
          :handle :johnny-as-hitching-post})

(def sample-person
  #:person{:id #uuid "b55f31fc-71cd-4996-81e0-6dad720e825b"
           :nickname "mako"
           :email "makoto@abc.com"
           :password "Qwe123!@#"
           :role :dancer
           :agreed? true})

(def sample-file
  #:file{:id #uuid "61a1da52-557c-45ea-a772-98e4700caabd"
         :post-id #uuid "b55f31fc-71cd-4996-81e0-6dad720e825b"
         :kind :image
         :objk "f9cec5bc-143c-431c-8796-494d22d36a69"
         :designation "zicos-profile-photo.jpg"
         :size 12345})

(def sample-profile
  #:profile{:id #uuid "74b4afdc-891e-46ab-8885-d44b4ed8ca78"
            :person-id #uuid "b55f31fc-71cd-4996-81e0-6dad720e825b"
            :phrase "sexy lovely"
            :place-ids #{#uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"}})

(t/deftest tick-test
  (t/is
   (m/validate
    model/tick
    sample-tick)))

(t/deftest address-test
  (t/is
   (m/explain
    model/address
    sample-address)))

(t/deftest place-test
  (t/is
   (m/validate
    model/place
    sample-place)))

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
        "abc@def.com"))))

(t/deftest password-test
  (let [f #(m/validate model/password %)]
    (t/testing "valid cases"
      (t/are [s] (f s)
        "Abc123xyz@"))
    (t/testing "invalid cases"
      (t/are [s] (not (f s))
        "abc12345678" "123" "abc"))))

(t/deftest person-test
  (t/is
   (m/validate
    model/person
    sample-person)))

(t/deftest file-test
  (t/is
   (m/validate
    model/file
    sample-file)))

(t/deftest profile-test
  (t/is
   (m/validate
    model/profile
    sample-profile)))
