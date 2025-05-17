(ns arcaneflare.env
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(defn profiles []
  (-> "profiles.edn"
      io/file slurp
      edn/read-string))

(defn current []
  (keyword
   (or (System/getProperty "env")
       (System/getenv "ENV")
       "dev")))

(defn config []
  (let [k (current)]
    (-> (profiles)
        (get k))))

(comment
  (config))