(ns tia.pages.storage
  (:require
   [tia.storage :as storage]
   [tia.layout :as l]))

(defn page [_request]
  (l/plain
   (str "Storage connection: "
        (-> (storage/get-buckets)
            first :name))))

(def routes
  ["/storage" {:get page}])
