(ns tia.db.session
  (:require
   [malli.core :as m]
   [tia.calc :as c]
   [tia.model :as model]
   [tia.db.common :as dbc]
   [tia.db.person :as db-person]
   [tia.util :as u]))

(defn coerce[m]
  (-> m c/kebab-m
      (u/map->nsmap :session)))

(u/mf coerce [:map model/session])

(defn get-all []
  (let [q {:select [:*]
           :from [:session]}]
    (map coerce (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:session{:id #uuid "f494f90c-2b36-4966-99cc-495aef967241",
                  :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                  :expired-at #inst "2024-03-26T18:45:28.647000000-00:00",
                  :created-at #inst "2024-03-23T18:45:28.647000000-00:00"}))

(defn create!
  [{:session/keys [id person-id created-at expired-at]
    :as session}]
  (assert (m/validate model/session session))
  (dbc/hd {:insert-into [:session]
           :columns [:id :person-id :created-at :expired-at]
           :values [[id person-id created-at expired-at]]}))

(comment
  (create!
   #:session{:id (u/uuid)
             :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f"
             :created-at (u/now)
             :expired-at (u/after-days 3)})
  :=> nil)

(defn get-session-by-id [id]
  (let [qr {:select [:session.*]
            :from [:session]
            :where [[:= :session.id id]]}]
    (map coerce (dbc/hq qr))))

(comment
  (get-session-by-id
   #uuid "d2c7f04b-a0cf-413c-a2e7-93b2e8c0a3f8"))

(defn get-session-and-person [id]
  (let [{:session/keys [person-id] :as session}
        (get-session-by-id id)
        person (db-person/get-by-id person-id)]
    {:session session
     :person person}))

(comment
  (get-session-and-person
   #uuid "d2c7f04b-a0cf-413c-a2e7-93b2e8c0a3f8")
  :=> {:session
       #:session{:id #uuid "d2c7f04b-a0cf-413c-a2e7-93b2e8c0a3f8",
                 :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                 :expired-at #inst "2024-03-26T18:52:34.390000000-00:00",
                 :created-at #inst "2024-03-23T18:52:34.390000000-00:00"},
       :person
       #:person{:id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                :nickname "monkey",
                :email "monkey@banana.com",
                :password "Abc123!@#",
                :job :customer,
                :verified? false,
                :created-at #inst "2024-03-23T13:46:18.309000000-00:00",
                :edited-at #inst "2024-03-23T13:46:18.309000000-00:00"}})

(defn find-all-by-email [email]
  (let [qr {:select [:session.*]
            :from [:session]
            :join [:person [:= :session.person-id :person.id]]
            :where [[:= :person.email email]]}]
    (map coerce (dbc/hq qr))))

(comment
  (find-all-by-email "monkey@banana.com")
  :=> '(#:session{:id #uuid "f494f90c-2b36-4966-99cc-495aef967241",
                  :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                  :expired-at #inst "2024-03-26T18:45:28.647000000-00:00",
                  :created-at #inst "2024-03-23T18:45:28.647000000-00:00"}
        #:session{:id #uuid "c6cb4f93-ee69-48db-a42c-708f61163dc7",
                  :person-id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                  :expired-at #inst "2024-03-26T18:45:43.092000000-00:00",
                  :created-at #inst "2024-03-23T18:45:43.092000000-00:00"}))

(defn find-by-email-and-pw [email password]
  (let [qr {:select [:session.*]
            :from [:session]
            :join [:person [:= :session.person-id :person.id]]
            :where [:and [:= :person.email email]
                    [:= :person.password password]]
            :order-by [[:session.expired-at :desc]]
            :limit 1}]
    (-> qr dbc/hq first)))

(comment
  (find-by-email-and-pw
   "monkey@banana.com"
   "Abc123!@#")
  :=> {:id #uuid "d2c7f04b-a0cf-413c-a2e7-93b2e8c0a3f8",
       :person_id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
       :expired_at #inst "2024-03-26T18:52:34.390000000-00:00",
       :created_at #inst "2024-03-23T18:52:34.390000000-00:00"})

(defn delete! [session-id]
  (let [qr {:delete-from [:session]
            :where [:= :session.id session-id]}]
    (dbc/hd qr)))

(comment
  (delete!
   #uuid "c6cb4f93-ee69-48db-a42c-708f61163dc7")
  :=> nil)

(defn count-sessions []
  (let [qr {:select [:%count.*]
            :from [:session]}]
    (-> qr dbc/hq first :count)))

(comment
  (count-sessions)
  :=> 6)
