(ns arcaneflare.database.person-test
  (:require
   [clojure.test :as t]
   [arcaneflare.database.fixture :as fxt]
   [arcaneflare.database.person :as sut]))

(t/use-fixtures
  :once
  fxt/migration-fixture)

(def sample-create-sql
  (sut/sql-create
   {:person/username "kokonut"
    :person/email "kokonut@abc.com"
    :person/job :job/owner}))

(def create-sql-intro
  (str "INSERT INTO people (id, username, email, job, "
       "verified, created_at, edited_at) VALUES "
       "(?, ?, ?, CAST(? AS JOBS), FALSE, ?, ?)"))

(t/deftest sql-create-test
  (t/is (= create-sql-intro (first sample-create-sql)))
  (t/is (= 7 (count sample-create-sql))))

(t/deftest db-create-test
  (fxt/execute! sample-create-sql))
