(ns tia.calc-test
  (:require
   [clojure.test :as t]
   [tia.calc :as src]))

(t/deftest >s-test
  (t/is (= "abc def"
           (src/>s :abc :def)))
  (t/is (= "keyup changed delay:500ms"
           (src/>s :keyup :changed :delay:500ms))))

(t/deftest nsmap->ns-test
  (t/is
   (= "abc"
      (src/nsmap->ns
       {:xt/id 7 :abc/x 1 :abc/y 2}))))

(t/deftest ns->idk-test
  (t/is
   (= :abc/id
      (src/ns->idk "abc"))))

(t/deftest nsmap->idk-test
  (t/is
   (= :abc/id
      (src/nsmap->idk
       {:abc/x 1 :abc/y 2}))))

(t/deftest ns->schema-test
  (t/is (vector? (src/ns->schema "address"))))

(t/deftest session-stringify-test
  (t/is
   (= "session-id=abc123;path=/"
      (src/session-stringify "abc123"))))

(t/deftest trim-low-test
  (t/is
   (= "hello"
      (src/trim-low " hElLo  "))))

(t/deftest handlify-test
  (t/are [address label handle]
      (= handle (src/handlify address label))
      {:address/state "Yokohama"
       :address/city "Kikuno"}
      "Platinum Dollz Gentlemens Lounge"
      :platinum-dollz-gentlemens-lounge
      {:address/state "Any"
       :address/city "Thing"}
      "Johnny A’s Hitching@ Post!"
      :johnny-as-hitching-post
      {:address/state "Ichibang"
       :address/city "Kokamachi"}
      "   My     Club    "
      :ichibang-kokamachi-my-club))

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

(t/deftest idify-test
  (t/is
   (= "abcdefg"
      (src/idify "A b  Cd ef g"))))

(t/deftest path-test
  (t/is
   (= "/abc/def/123"
      (src/path "abc" :def "123")))
  (t/is
   (= "/abc/016ab17d-4675-4078-a81b-194cd93dc1fc"
      (src/path :abc #uuid "016ab17d-4675-4078-a81b-194cd93dc1fc"))))
