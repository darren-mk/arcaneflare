(ns tia.db.person
  (:require
   [clojure.set :as cset]
   [malli.core :as m]
   [tia.db.common :as dbc]
   [tia.model :as model]
   [tia.util :as u]))

(m/=> coerce
      [:=> [:cat :map]
       model/person])

(defn coerce [m]
  (let [renaming {:verified :verified?
                  :created_at :created-at
                  :edited_at :edited-at}]
    (-> (cset/rename-keys m renaming)
        (u/update-if-exists :job keyword)
        (u/map->nsmap :person))))

(defn get-all []
  (let [q {:select [:*]
           :from [:person]}]
    (map coerce (dbc/hq q))))

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
  [{:person/keys [id nickname email password job verified?
                  created-at edited-at] :as person}]
  (assert (m/validate model/person person))
  (dbc/hd {:insert-into [:person]
           :columns [:id :nickname :email :password
                     :job :verified :created_at :edited_at]
           :values [[id nickname email password (name job) verified?
                     created-at edited-at]]}))

(comment
  (create!
   #:person{:id (u/uuid)
            :nickname "monkey"
            :email "monkey@banana.com"
            :password "Abc123!@#"
            :job :customer
            :verified? false
            :created-at (u/now)
            :edited-at (u/now)})
  :=> nil)

(defn get-by-id [id]
  (let [code {:select [:person.*]
              :from [:person]
              :where [:= :person.id id]}]
    (-> code dbc/hq first coerce)))

(comment
  (get-by-id
   #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f"))

(defn nickname-exists? [s]
  (let [code {:select [:%count.*]
              :from [:person]
              :where [:= :nickname s]}]
    (-> code dbc/hq first :count pos?)))

(comment
  (nickname-exists? "kokonut")
  :=> true)

(defn email-exists? [s]
  (let [code {:select [:%count.*]
              :from [:person]
              :where [:= :email s]}]
    (-> code dbc/hq first :count pos?)))

(comment
  (email-exists? "koko@nut.com")
  :=> true)

(defn count-all []
  (let [code {:select [:%count.*]
              :from [:person]}]
    (-> code dbc/hq first :count)))

(comment
  (count-all)
  :=> 2)

(defn find-by-email [email]
  (let [code {:select [:*]
              :from [:person]
              :where [:= :email email]}]
    (->> code dbc/hq
         (map coerce))))

(comment
  (find-by-email "monkey@banana.com")
  :=> '(#:person{:id #uuid "487d9a9c-ca17-4dc5-9d34-b4c1fe48b04f",
                 :nickname "monkey",
                 :email "monkey@banana.com",
                 :password "Abc123!@#",
                 :job :customer,
                 :verified? false,
                 :created-at #inst "2024-03-23T13:46:18.309000000-00:00",
                 :edited-at #inst "2024-03-23T13:46:18.309000000-00:00"}))
