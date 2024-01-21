(ns tia.db.session
  (:require
   [tia.model]
   [tia.db.common :as com]
   [tia.db.person :as pdb]
   [tia.util :as u]))

(defn find-by-id [id]
  (ffirst
   (com/query
    '{:find [(pull ?session [*])]
      :in [[?id]]
      :where [[?session :session/id ?id]]}
    [id])))

(comment
  (find-by-id #uuid "4b02e9b1-2b40-40a9-a57c-878ddcddba93")
  :=> {:session/id #uuid "4b02e9b1-2b40-40a9-a57c-878ddcddba93"
       :session/person.id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e"
       :session/expiration #inst "2024-01-20T21:21:38.248-00:00"
       :xt/id #uuid "4b02e9b1-2b40-40a9-a57c-878ddcddba93"})

(defn find-all-by-email [email]
  (mapv first
        (com/query
         '{:find [(pull ?session [*])]
           :in [[?email]]
           :where [[?session :session/person.id ?pid]
                   [?person :person/id ?pid]
                   [?person :person/email ?email]]}
         [email])))

(comment
  (find-all-by-email "kokonut@abc.com")
  :=> [{:session/id #uuid "f1e86d8a-7102-4640-8622-950cde242c8e",
        :session/person.id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
        :session/expiration #inst "2024-02-20T12:40:07.918-00:00",
        :xt/id #uuid "f1e86d8a-7102-4640-8622-950cde242c8e"}
       {:session/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f",
        :session/person.id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
        :session/expiration #inst "2024-02-20T12:44:59.414-00:00",
        :xt/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f"}])

(defn create! [session]
  (com/record! session))

(comment
  (create!
   {:session/id #uuid "4b02e9b1-2b40-40a9-a57c-878ddcddba93"
    :session/person.id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e"
    :session/expiration #inst "2024-01-20T21:21:38.248-00:00"})
  :=> #:xtdb.api{:tx-id 502, :tx-time #inst "2024-01-20T21:22:42.061-00:00"})

(defn login! [{:keys [email password]}]
  (let [person (pdb/find-by-email email)
        pid (:person/id person)
        session {:session/id (u/uuid)
                 :session/person.id pid
                 :session/expiration (u/after-days 30)}
        matched? (= password (:person/password person))]
    (when (and matched? (:xtdb.api/tx-id (create! session)))
      session)))

(comment
  (login! {:email "jackie@abc.com"
          :password "not-password"})
  :=> nil
  (login! {:email "jackie@abc.com"
          :password "Abc123!@#"})
  :=> #:session{:id #uuid "5d400d9f-f5d5-4b82-8045-082ed6108f52",
                :person.id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e",
                :expiration #inst "2024-01-20T21:21:38.248-00:00"})

(defn count-sessions []
  (com/count-all-having-key :session/id))

(comment
  (count-sessions)
  :=> 6)
