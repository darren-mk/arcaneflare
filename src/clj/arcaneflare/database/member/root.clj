(ns arcaneflare.database.member.root
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
           :values [[(or id (random-uuid))
                     username email role
                     passcode-hash [:raw "now()"]]]}]
    (-> (db.base/run q) first vals first)))

(defn get-by
  [{:member/keys [id username email token]}]
  (let [where (cond id [:= :id id]
                    username [:= :username username]
                    email [:= :email email]
                    token [:= :id (-> token token/unsign :member/id)])
        q {:select [:*]
           :from :member
           :where where}]
    (-> q db.base/run first)))

(defn remove!
  [{member-id :member/id}]
  (let [q {:delete-from :member
           :where [:= :id member-id]}]
    (db.base/run q)))

(defn authenticate [username passcode]
  (let [{:keys [member/passcode-hash] :as member}
        (get-by {:member/username username})
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
  (let [{:member/keys [id role] :as member}
        (authenticate username passcode)]
    (when-not member
      (throw (ex-info "no match" {:error :not-found})))
    (update-last-login! {:member/id id})
    {:member member
     :token (token/gen! {:member/id id
                         :member/role role})}))