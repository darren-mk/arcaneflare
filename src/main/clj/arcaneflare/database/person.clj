(ns arcaneflare.database.person
  (:require
   [arcaneflare.util :as u]
   [arcaneflare.common.schema]
   [arcaneflare.database.core :as dbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(defn create-one
  [{:person/keys [id username email job verified
                  created-at edited-at]}]
  (let [v [(or id (u/uuid)) username email
           [:cast (name job) :job] (or verified false)
           (or created-at (u/now)) (or edited-at (u/now))]]
    (-> (h/insert-into :person)
        (h/columns :id :username :email :job
                   :verified :created-at :edited-at)
        (h/values [v]) sql/format)))

(defn sql-get-one-by-email [email]
  (-> (h/select :*)
      (h/from :person)
      (h/where [:= :email email])
      sql/format))

(defn db-get-one-by-email [email]
  (->> (sql-get-one-by-email email)
       (dbc/execute-one! dbc/*ds*)))


