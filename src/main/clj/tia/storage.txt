(ns tia.storage
  (:require
   [amazonica.aws.s3 :as s3]
   [malli.core :as m]
   [mount.core :refer [defstate]]
   [tia.config :refer [env]]
   [tia.db.file :as db-file]
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

(def upload-file
  (m/-instrument
   {:schema [:=> [:cat model/file :any] :any]}
   (fn [{:file/keys [objk] :as file} data]
     (db-file/create! file)
     (s3/put-object
      aws
      :bucket-name s3-bucket
      :key objk
      :file data))))

(defn get-data [objk]
  (-> (s3/get-object
       aws s3-bucket objk)
      :input-stream
      slurp))

(comment
  (get-data "toto")
  :=> "test\nyoyoyo\n")

(defn presign-url [objk]
  (let [expiration (u/after-minutes 5)]
    (u/obj->str
     (s3/generate-presigned-url
      aws s3-bucket objk expiration))))

(comment
  (presign-url "toto")
  :=> "https://purple-...s3.amazonaws.com/toto?X-Amz-Algorithm=AWS4...")

(defn delete-file [objk]
  (s3/delete-object
   aws
   :bucket-name s3-bucket
   :key objk)
  (db-file/delete-file-by-objk objk))

(comment
  (let [objks '("1f229f7b-115f-43df-823e-fee9fd43fa52"
                "ba509037-25b9-4ea1-b604-f659fb8c831e"
                "e21560f4-3730-4fcd-b96e-dfb7adc82f9b"
                "53e2fc2b-2c4d-4211-9c51-b434338d5b2c"
                "7f71bb5d-4734-40fc-8ca8-16d9855dcbd3"
                "6324a81a-eed7-41e1-baf2-44c88d83975c"
                "1f6efc61-6691-4b2b-a179-697776022636"
                "be3ed809-811f-48ea-aa9b-36c8c11501c1"
                "52bbc22b-de03-4acd-9896-547cd5561e4a")]
    (doseq [objk objks]
      (delete-file objk)))
  :=> nil)
