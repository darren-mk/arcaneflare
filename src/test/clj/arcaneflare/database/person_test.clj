(ns arcaneflare.database.person-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.test-core :as tcr]
   [arcaneflare.database.person :as sut]))

(t/use-fixtures
  :once
  tcr/migration-fixture)

(def sample-person-a
  #:person{:username "kokonut"
           :email "kokonut@abc.com"
           :job :customer :verified true})

(def sample-person-b
  #:person{:username "mohito" :email "mohito@abc.com"
           :job :provider :verified false})

(def sample-create-sql
  (sut/create-one sample-person-a))

(t/deftest sql-create-test
  (t/is (= (str "INSERT INTO person (id, username, email, job, "
                "verified, created_at, edited_at) VALUES "
                "(?, ?, ?, CAST(? AS JOB), TRUE, ?, ?)")
           (first sample-create-sql)))
  (t/is (= 7 (count sample-create-sql))))

(t/deftest db-create-test
  (tcr/execute! tcr/test-db-spec sample-create-sql))

(t/deftest sql-get-one-by-email-test
  (t/is (= ["SELECT * FROM person WHERE email = ?"
            "kokonut@abc.com"]
           (sut/sql-get-one-by-email "kokonut@abc.com"))))

(t/deftest get-one-by-email-test
  (->> sample-person-b
       sut/create-one
       (tcr/execute-one! tcr/test-db-spec))
  (let [sql (-> (get sample-person-b :person/email)
                sut/sql-get-one-by-email)]
    (t/is (= (-> (tcr/execute-one! tcr/test-db-spec sql)
                 (select-keys [:person/username :person/email
                               :person/job :person/verified]))
             #:person{:username "mohito"
                      :email "mohito@abc.com"
                      :job "provider"
                      :verified false}))))
