(ns tia.util)

(defn uuid []
  (java.util.UUID/randomUUID))

(comment
  (uuid)
  :=> #uuid "0763aeba-a426-4b3f-bee5-616ec784f151")

(defn now []
  (java.util.Date.))
