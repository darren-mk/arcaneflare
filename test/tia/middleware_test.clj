(ns tia.middleware-test
  (:require
   [clojure.test :as t]
   [tia.db.session :as session-db]
   [tia.middleware :as src]))

#_
(t/deftest sessionize-test
  (let [cookies {"session-id" {:value "abc"}}
        handler (src/sessionize identity)]
    (t/testing "neither expired nor renewing"
      (let [session {:session/id 123
                     :session/renewal #inst "2098-02-21T15:43:47.621-00:00"
                     :session/expiration #inst "2099-02-21T15:43:47.621-00:00"}]
        (with-redefs [session-db/find-by-id
                      (constantly session)]
          (t/is
           (= {:a 1 :session session :cookies cookies}
              (handler {:a 1 :cookies cookies}))))))
    (t/testing "not expired but renewing"
      (let [session {:session/id 123
                     :session/renewal #inst "2000-02-21T15:43:47.621-00:00"
                     :session/expiration #inst "2099-02-21T15:43:47.621-00:00"}]
        (with-redefs [session-db/find-by-id
                      (constantly session)]
          (t/is
           (= {:a 1 :session session :cookies cookies
               :headers {"Set-Cookie" "session-id=;path=/"}}
              (handler {:a 1 :cookies cookies}))))))
    (t/testing "expired"
      (let [session {:session/id 123
                     :session/renewal #inst "2000-02-21T15:43:47.621-00:00"
                     :session/expiration #inst "2001-02-21T15:43:47.621-00:00"}]
        (with-redefs [session-db/find-by-id
                      (constantly session)]
          (t/is
           (= {:a 1 :cookies cookies}
              (handler {:a 1 :cookies cookies}))))))))
