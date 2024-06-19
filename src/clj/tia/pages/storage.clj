(ns tia.pages.storage
  (:require
   #_[tia.storage :as storage]
   #_[tia.layout :as l]))

#_
(defn page [_request]
  (l/plain
   (str "Storage connection: "
        (-> (storage/get-buckets)
            first :name))))

#_
(def routes
  ["/storage" {:get page}])
