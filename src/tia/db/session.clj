(ns tia.db.session
  (:require
   [malli.core :as m]
   [tia.model :as md]
   [tia.db.common :as db-common]
   [tia.db.person :as pdb]
   [tia.util :as u]))

(m/=> get-session-and-person
      [:=> [:cat uuid?] :any])

(defn get-session-and-person [id]
  (first
   (db-common/query
    '{:find [(pull ?session [*])
             (pull ?person [*])]
      :keys [session person]
      :in [[?id]]
      :where [[?session :session/id ?id]
              [?session :session/person-id ?pid]
              [?person :person/id ?pid]]}
    [id])))

(comment
  (get-session-and-person
   #uuid "be350684-2dd4-4875-be28-f923532540ed")
  :=> {:session
       {:session/id #uuid "be350684-2dd4-4875-be28-f923532540ed",
        :session/person-id #uuid "11381509-5e3b-448b-958d-6a23b242ce61",
        :session/renewal #inst "2024-03-08T16:59:18.357-00:00",
        :session/expiration #inst "2024-03-11T16:59:18.357-00:00",
        :xt/id #uuid "be350684-2dd4-4875-be28-f923532540ed"},
       :person
       {:person/id #uuid "11381509-5e3b-448b-958d-6a23b242ce61",
        :person/nickname "kokonut",
        :person/email "kokonut@koko.nut",
        :person/password "Abc123!@#",
        :person/role :customer,
        :person/agreed? true,
        :xt/id #uuid "11381509-5e3b-448b-958d-6a23b242ce61"}})

(defn find-all-by-email [email]
  (mapv first
        (db-common/query
         '{:find [(pull ?session [*])]
           :in [[?email]]
           :where [[?session :session/person-id ?pid]
                   [?person :person/id ?pid]
                   [?person :person/email ?email]]}
         [email])))

(comment
  (find-all-by-email "kokonut@abc.com")
  :=> [{:session/id #uuid "f1e86d8a-7102-4640-8622-950cde242c8e",
        :session/person-id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
        :session/expiration #inst "2024-02-20T12:40:07.918-00:00",
        :xt/id #uuid "f1e86d8a-7102-4640-8622-950cde242c8e"}
       {:session/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f",
        :session/person-id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
        :session/expiration #inst "2024-02-20T12:44:59.414-00:00",
        :xt/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f"}])

(m/=> session-instance
      [:=> [:cat :uuid] md/session])

(defn session-instance [pid]
  {:session/id (u/uuid)
   :session/person-id pid
   :session/renewal (u/after-days 27)
   :session/expiration (u/after-days 30)})

(defn login!
  ([person-id]
   (let [session (session-instance person-id)]
     (when (:xtdb.api/tx-id (db-common/record! session))
       session)))
  ([email password]
   (let [person (pdb/find-by-email email)
         person-id (:person/id person)
         matched? (= password (:person/password person))
         session (session-instance person-id)]
     (when (and matched? (:xtdb.api/tx-id (db-common/record! session)))
       session))))

(comment
  (login! "jackie@abc.com" "not-password")
  :=> nil
  (login! "jackie@abc.com" "Abc123!@#")
  :=> #:session{:id #uuid "7185156a-cde1-42d4-ada6-805c9d39cb10",
                :person-id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e",
                :renewal #inst "2024-02-18T15:48:24.225-00:00",
                :expiration #inst "2024-02-21T15:48:24.225-00:00"})

(defn logout! [session-id]
  (db-common/delete! session-id))

(defn count-sessions []
  (db-common/count-all-having-key :session/id))

(comment
  (count-sessions)
  :=> 6)
