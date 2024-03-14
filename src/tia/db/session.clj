(ns tia.db.session
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.model :as md]
   [tia.db.common :as dbc]
   [tia.db.person :as pdb]
   [tia.util :as u]))

(defonce cache
  (atom {}))

(defn translate[m]
  (let [renaming {:person_id :person-id
                  :expired_at :expired-at}
        session (-> (cset/rename-keys m renaming)
                    (u/map->nsmap :session))]
    (m/coerce md/session session)))

(defn get-all []
  (let [q {:select [:*]
           :from [:session]}]
    (map translate (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:session{:id #uuid "cfc0995e-c64b-46fe-9887-99f2ad735c0d",
                  :person-id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c",
                  :expired-at #inst "1969-12-31T05:00:00.000000000-00:00"}))

(defn create!
  [{:session/keys [id person-id expired-at] :as session}]
  (assert (m/validate md/session session))
  (dbc/hd {:insert-into [:sessions]
           :columns [:id :person-id :expiration]
           :values [[id person-id
                     [:cast expired-at :timestamp]]]}))

(comment
  (create!
   #:session{:id (u/uuid)
             :person-id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c"
             :expired-at (u/now)})
  :=> nil)

(m/=> get-session-and-person
      [:=> [:cat uuid?] :any])

(defn get-session-and-person [id]
  (first
   (dbc/query
    '{:find [(pull ?session [*])
             (pull ?person [*])]
      :keys [session person]
      :in [[?id]]
      :where [[?session :session/id ?id]
              [?session :session/person-id ?pid]
              [?person :person/id ?pid]]}
    [id])))

(defn find-all-by-email [email]
  (mapv first
        (dbc/query
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
        :session/expired-at #inst "2024-02-20T12:40:07.918-00:00",
        :xt/id #uuid "f1e86d8a-7102-4640-8622-950cde242c8e"}
       {:session/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f",
        :session/person-id #uuid "a3a9e552-773e-4b3b-9594-4c0fa5e6c79e",
        :session/expired-at #inst "2024-02-20T12:44:59.414-00:00",
        :xt/id #uuid "028a68e8-ae32-4d08-b7ae-e1201ba2cf5f"}])

(m/=> instantiate
      [:=> [:cat :uuid] md/session])

(defn instantiate [pid]
  {:session/id (u/uuid)
   :session/person-id pid
   :session/renewal (u/after-days 27)
   :session/expired-at (u/after-days 30)})

(defn login!
  ([person-id]
   (let [session (instantiate person-id)]
     (when (:xtdb.api/tx-id (dbc/record! session))
       session)))
  ([email password]
   (let [person (pdb/find-by-email email)
         person-id (:person/id person)
         matched? (= password (:person/password person))
         {:session/keys [id] :as session} (instantiate person-id)]
     (swap! cache assoc-in [id :session] session)
     (swap! cache assoc-in [id :person] person)
     (when (and matched? (:xtdb.api/tx-id (dbc/record! session)))
       session))))

(comment
  (login! "jackie@abc.com" "not-password")
  :=> nil
  (login! "jackie@abc.com" "Abc123!@#")
  :=> #:session{:id #uuid "7185156a-cde1-42d4-ada6-805c9d39cb10",
                :person-id #uuid "c864fd4b-ec4b-4310-8d69-e1be290cd57e",
                :renewal #inst "2024-02-18T15:48:24.225-00:00",
                :expired-at #inst "2024-02-21T15:48:24.225-00:00"})

(defn logout! [session-id]
  (swap! cache assoc session-id :out)
  (dbc/delete! session-id))

(defn count-sessions []
  (dbc/count-all-having-key :session/id))

(comment
  (count-sessions)
  :=> 6)
