(ns tia.db.person
  (:require
   [tia.db.common :as com]))

(defn nickname-existent? [s]
  (pos? (com/count-all-having-kv
         :person/nickname s)))

(comment
  (nickname-existent? "abc")
  :=> false)

(defn email-existent? [s]
  (pos? (com/count-all-having-kv
         :person/email s)))

(comment
  (email-existent? "abc@def.com")
  :=> false)
