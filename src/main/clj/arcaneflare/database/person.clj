(ns arcaneflare.database.person
  (:require
   [arcaneflare.database.core :as dbc]
   [arcaneflare.util :as u]
   [clojure.spec.alpha :as s]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn sql-create
  [{:person/keys [username email job]}]
  (let [v [(u/uuid) username email [:cast (name job) :jobs]
           false (u/now) (u/now)]]
    (-> (h/insert-into :people)
        (h/columns :id :username :email :job
                   :verified :created-at :edited-at)
        (h/values [v]) sql/format)))

(defn create! [person]
  (s/assert :person/object person)
  (dbc/execute! (sql-create person)))

(defn get-by-email [email]
  (-> (h/select :*)
      (h/from :people)
      (h/where [:= :email email])
      sql/format
      dbc/execute!))

(comment
  (get-by-email "kokonut@abc.com")
  :=> [#:people{:id #uuid "d1dab5d4-8c03-42bb-ae18-ec416583b5ef"
                :username "kokonut"
                :email "kokonut@abc.com"
                :job "owner"
                :verified false
                :created_at #inst "2024-07-04T21:27:35.333000000-00:00"
                :edited_at #inst "2024-07-04T21:27:35.333000000-00:00"}])
