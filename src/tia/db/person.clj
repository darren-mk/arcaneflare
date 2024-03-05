(ns tia.db.person
  (:require
   [tia.db.common :as dbcm]))

(defn convert [{:keys [id nickname email
                       password job verified]}]
  #:person{:id id
           :nickname nickname
           :email email
           :password password
           :job (keyword job)
           :verified verified})

(defn get-all []
  (map
   convert
   (dbcm/hq
    {:select [:*]
     :from [:persons]})))

(comment
  (get-all)
  :=> '(#:person{:id #uuid "b55f31fc-71cd-4996-81e0-6dad720e825b",
                 :nickname "mako",
                 :email "makoto@abc.com",
                 :password "Qwe123!@#",
                 :job :customer,
                 :verified false}
        #:person{:id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c",
                 :nickname "kokonut",
                 :email "koko@nut.com",
                 :password "Abc123!@#",
                 :job :customer,
                 :verified false}))

(defn create!
  [{:person/keys [id nickname email password job verified]}]
  (dbcm/hd {:insert-into [:persons]
            :columns [:id :nickname :email :password :job :verified]
            :values [[id nickname email password
                      [:cast (name job) :jobs] verified]]}))

(comment
  (create!
   #:person{:id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c"
            :nickname "kokonut"
            :email "koko@nut.com"
            :password "Abc123!@#"
            :job :customer
            :verified false})
  :=> nil)

(defn nickname-exists? [s]
  (let [code {:select [:%count.*]
              :from [:persons]
              :where [:= :nickname s]}]
    (-> code dbcm/hq first :count pos?)))

(comment
  (nickname-exists? "kokonut")
  :=> true)

(defn email-exists? [s]
  (let [code {:select [:%count.*]
              :from [:persons]
              :where [:= :email s]}]
    (-> code dbcm/hq first :count pos?)))

(comment
  (email-exists? "koko@nut.com")
  :=> true)

(defn count-all []
  (let [code {:select [:%count.*]
              :from [:persons]}]
    (-> code dbcm/hq first :count)))

(comment
  (count-all)
  :=> 2)

(defn find-by-email [email]
  (let [code {:select [:*]
              :from [:persons]
              :where [:= :email email]}
        raw (-> code dbcm/hq first)
        {:keys [id nickname email password job verified]} raw]
    #:person{:id id
             :nickname nickname
             :email email
             :password password
             :job (keyword job)
             :verified verified}))

(comment
  (find-by-email "koko@nut.com")
  :=> #:person{:id #uuid "4dd28ba4-c12c-4367-ab1d-1548f9c9764c"
               :nickname "kokonut"
               :email "koko@nut.com"
               :password "Abc123!@#"
               :job :customer
               :verified false})
