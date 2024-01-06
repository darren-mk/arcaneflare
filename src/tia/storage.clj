(ns tia.storage
  (:require
   [amazonica.aws.s3 :as s3]
   [tia.config :refer [env]]))

(def cred
  {:access-key (:aws-access-key env)
   :secret-key (:aws-secret-key env)
   :endpoint (:aws-region env)})

(def bucket
  (:aws-s3-bucket env))

(defn get-buckets []
  (s3/list-buckets cred))

(comment
  (get-buckets))

(comment
  (s3/put-object
   cred
   :bucket-name bucket
   :key "toto"
   :file (java.io.File. "ttt.txt")))

(comment
  (-> (s3/get-object cred bucket "toto")
      :input-stream
      slurp
      time))
