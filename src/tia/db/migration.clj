(ns tia.db.migration
  (:require
   [clojure.edn :as cedn]
   [clojure.java.io :as io]
   [clojure.string :as cstr]
   [clojure.tools.logging :as log]
   [malli.core :as m]
   [tia.db.common :as common]
   [tia.model :as model]
   [tia.util :as u]))

(defn single! [{:migration/keys [file] :as mig}]
  (let [content (->> file (str "migrations/")
                     io/resource slurp
                     cedn/read-string)]
    (log/info "migration tag:" mig)
    (common/merge! mig)
    (log/info "migration content:" content)
    (doseq [item content]
      (common/merge! item))))

(defn extract [filename]
  (let [[time-s id _] (cstr/split filename #"[_\.]")
        time (u/read-time-s time-s)]
    #:migration{:time time
                :id (keyword id)
                :file filename}))

(defn migrated?
  [{:keys [id] :as _migration}]
  (seq (common/pull-by-id id)))

(defn all! []
  (let [filenames (u/file-names! "resources/migrations")
        migrations (map extract filenames)
        n-migs (remove migrated? migrations)]
    (doseq [n-mig n-migs]
      (single! n-mig)
      (log/info n-mig "is just migrated."))))

(m/=> single!
      [:=> [:cat model/migration]
       :any])

(m/=> extract
      [:=> [:cat :string]
       model/migration])
