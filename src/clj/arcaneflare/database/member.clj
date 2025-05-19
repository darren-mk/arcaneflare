(ns arcaneflare.database.member
  (:require
   [buddy.hashers :as hashers]
   [hugsql.core :as hugsql]
   [arcaneflare.database.base :as db.base]
   [arcaneflare.token :as token]))

(defn hash-passcode [plain]
  (hashers/derive plain))

(defn verify-passcode [plain hashed]
  (hashers/check plain hashed))

(hugsql/def-db-fns "sql/member.sql")
(declare insert-member!)
(declare get-member-by-id)
(declare get-member-by-username)
(declare get-member-by-email)
(declare update-last-login!)

(defn insert! [{:keys [passcode] :as member}]
  (let [hashed (hash-passcode passcode)
        member' (-> member (dissoc :passcode)
                    (assoc :passcode_hash hashed)
                    (assoc :id (random-uuid)))]
    (insert-member!
     db.base/db member')))

(defn member-by
  [{:keys [id username email]}]
  (cond id (get-member-by-id
            db.base/db {:id id})
        username (get-member-by-username
                  db.base/db {:username username})
        email (get-member-by-email
               db.base/db {:email email})))

(defn authenticate [username passcode]
  (let [{:keys [passcode_hash] :as member}
        (member-by {:username username})
        verified? (verify-passcode
                   passcode passcode_hash)]
    (and verified? member)))

(defn login! [username passcode]
  (when-let [{:keys [id username role] :as _member}
             (authenticate username passcode)]
    (update-last-login! db.base/db {:id id})
    (token/gen! {:id id :role role
                 :username username})))

(comment
  (authenticate "masterplan" "fakepasscode")
  (insert! {:username "masterplan"
            :email "master@plan.com"
            :role "admin"
            :passcode "fakepasscode"})
  (let [hashed (hash-passcode "fakepasscode")]
    (insert-member!
     db.base/db {:id (random-uuid)
                 :username "darren"
                 :email "darren@example.com"
                 :role "admin"
                 :passcode_hash hashed}))
  (verify-passcode
   "fakepasscode"
   "bcrypt+sha512$31cb41...")
  (time (member-by {:username "masterplan"})))
