(ns tia.storage
  (:require
   [amazonica.aws.s3 :as s3]
   [mount.core :refer [defstate]]
   [tia.config :refer [env]]))

(declare aws)

(defstate ^:dynamic aws
  :start {:access-key (:aws-access-key-id env)
          :secret-key (:aws-secret-access-key env)
          :endpoint (:aws-region env)}
  :stop nil)

(declare ^:dynamic s3b)

(defstate s3b
  :start (:aws-s3-bucket env)
  :stop nil)

(defn get-buckets []
  (s3/list-buckets aws))

(comment
  (get-buckets))

(comment
  (s3/put-object aws
   :bucket-name s3b
   :key "toto"
   :file (java.io.File. "ttt.txt")))

(comment
  (-> (s3/get-object aws s3b "toto")
      :input-stream
      slurp
      time))
