(ns arcaneflare.database.base
  (:require
   [honey.sql :as sql]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [arcaneflare.env :as env]))

(def db
  (get (env/config) :pg))

(def ds
  (jdbc/get-datasource db))

(defn exc [sqls]
  (jdbc/execute!
   ds sqls
   {:builder-fn
    rs/as-kebab-maps}))

(def run
  (comp exc honey.sql/format))

(comment
  (exc ["select * from place_love limit 1"])
  :=> [#:place-love{:member-id #uuid "273e92d4-8134-400e-907d-469227f5dc6e",
                    :place-id #uuid "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                    :loved-at #inst "2025-05-20T13:52:03.291361000-00:00"}]
  (run {:select [:*]
        :from :member}))
