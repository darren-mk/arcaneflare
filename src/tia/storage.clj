(ns tia.storage
  (:require
   [amazonica.aws.s3 :as s3]
   [malli.core :as m]
   [mount.core :refer [defstate]]
   [tia.config :refer [env]]
   [tia.db.image :as image-db]
   [tia.model :as model]
   [tia.util :as u]))

(declare aws)

(defstate ^:dynamic aws
  :start {:access-key (:aws-access-id env)
          :secret-key (:aws-access-key env)
          :endpoint (:aws-region env)}
  :stop nil)

(declare ^:dynamic s3-bucket)

(defstate s3-bucket
  :start (:aws-s3-bucket env)
  :stop nil)

(defn get-buckets []
  (s3/list-buckets aws))

(comment
  (get-buckets)
  :=> [{:name "purple-lights-storage"
        :owner {:id "b90ae..."
                :display-name "darrenkim"}
        :creation-date "#object..."}])

(m/=> upload-image
      [:=> [:cat model/image :any]
       :any])

(defn upload-image
  [{:keys [image/objk] :as image} file]
  (image-db/create! image)
  (s3/put-object
   aws
   :bucket-name s3-bucket
   :key objk
   :file file))

(defn get-file [objk]
  (-> (s3/get-object
       aws s3-bucket objk)
      :input-stream
      slurp))

(comment
  (get-file "toto")
  :=> "test\nyoyoyo\n")

(defn presign-url [objk]
  (let [expiration (u/after-minutes 5)]
    (u/obj->str
     (s3/generate-presigned-url
      aws s3-bucket objk expiration))))

(comment
  (presign-url "toto")
  :=> "https://purple-...s3.amazonaws.com/toto?X-Amz-Algorithm=AWS4...")
