(ns arcaneflare.database.person
  (:require
   [arcaneflare.util :as u]
   [arcaneflare.common.schema]
   [arcaneflare.database.core :as dbc]
   [clojure.spec.alpha :as s]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(s/fdef create-one
  :args (s/cat :person :person/object)
  :ret any?)

(defn create-one
  [{:person/keys [id username email job verified
                  created-at edited-at] :as _person}]
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

(s/fdef db-get-one-by-email
  :args (s/cat :email :person/email)
  :ret :person/object)

(defn db-get-one-by-email [email]
  (->> email sql-get-one-by-email
       (dbc/execute-one! dbc/*ds*)))

