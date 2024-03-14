(ns tia.db.person
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(defn translate [m]
  (let [renaming {:verified :verified?
                  :created_at :created-at
                  :edited_at :edited-at}
        person (-> (cset/rename-keys m renaming)
                   (update :job keyword)
                   (u/map->nsmap :person))]
    (m/coerce model/person person)))

(defn get-all []
  (let [q {:select [:*]
           :from [:person]}]
    (map translate (dbc/hq q))))

(comment
  (take 1 (get-all))
  :=> '(#:person{:id #uuid "b55f31fc-71cd-4996-81e0-6dad720e825b",
                 :nickname "mako",
                 :email "makoto@abc.com",
                 :password "Qwe123!@#",
                 :job :customer,
                 :verified? false,
                 :created-at #inst "2024-03-14T07:49:24.548449000-00:00",
                 :edited-at #inst "2024-03-14T07:49:24.548449000-00:00"}))

(defn create!
  [{:keys [person_id nickname email password job verified] :as person}]
  (assert (m/validate model/person person))
  (dbc/hd {:insert-into [:person]
            :columns [:person_id :nickname :email :password :job :verified]
            :values [[person_id nickname email password (name job) verified]]}))

(comment
  (create!
   {:person_id (u/uuid)
    :nickname "monkey"
    :email "monkey@banana.com"
    :password "Abc123!@#"
    :job :customer
    :verified false})
  :=> nil)

(defn nickname-exists? [s]
  (let [code {:select [:%count.*]
              :from [:persons]
              :where [:= :nickname s]}]
    (-> code dbc/hq first :count pos?)))

(comment
  (nickname-exists? "kokonut")
  :=> true)

(defn email-exists? [s]
  (let [code {:select [:%count.*]
              :from [:persons]
              :where [:= :email s]}]
    (-> code dbc/hq first :count pos?)))

(comment
  (email-exists? "koko@nut.com")
  :=> true)

(defn count-all []
  (let [code {:select [:%count.*]
              :from [:persons]}]
    (-> code dbc/hq first :count)))

(comment
  (count-all)
  :=> 2)

(defn find-by-email [email]
  (let [code {:select [:*]
              :from [:persons]
              :where [:= :email email]}
        raw (-> code dbc/hq first)
        {:keys [id nickname email password job verified]} raw
        person #:person{:id id
                        :nickname nickname
                        :email email
                        :password password
                        :job (keyword job)
                        :verified verified}]
    (m/coerce model/person person)))

(comment
  (find-by-email "koko@nut.com")
  :=> #:person{:id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c"
               :nickname "kokonut"
               :email "koko@nut.com"
               :password "Abc123!@#"
               :job :customer
               :verified false})
