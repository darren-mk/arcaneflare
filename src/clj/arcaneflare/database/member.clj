(ns arcaneflare.database.member
  (:require
   [buddy.hashers :as hashers]
   [honey.sql :as sql]
   [arcaneflare.database.base :as db.base]
   [arcaneflare.token :as token]))

(defn insert!
  [{:member/keys [id username email role passcode]}]
  (let [passcode-hash (hashers/derive passcode)
        q {:insert-into :member
           :columns [:id :username :email :role
                     :passcode_hash :created_at]
           :values [[id username email role
                     passcode-hash [:raw "now()"]]]}]
    (db.base/exc (honey.sql/format q))))

(defn member-by
  [{:member/keys [id username email]}]
  (let [where (cond id [:= :id id]
                    username [:= :username username]
                    email [:= :email email])
        q {:select [:*]
           :from :member
           :where where}]
    (first (db.base/exc (sql/format q)))))

(defn- authenticate [username passcode]
  (let [{:keys [member/passcode-hash] :as member}
        (member-by {:member/username username})
        verified? (hashers/check
                   passcode passcode-hash)]
    (and verified? member)))

(defn update-last-login!
  [{:member/keys [id]}]
  (let [q {:update :member
           :set {:last-login [:raw "now()"]}
           :where [:= :id id]}]
    (db.base/exc (sql/format q))))

(defn login! [{:member/keys [username passcode]}]
  (when-let [{:member/keys [id role] :as _member}
             (authenticate username passcode)]
    (update-last-login! {:member/id id})
    (token/gen! {:member/id id
                 :member/role role})))
