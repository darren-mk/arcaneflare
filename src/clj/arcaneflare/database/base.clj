(ns arcaneflare.database.base
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [honey.sql :as sql]
   [mount.core :as m]
   [next.jdbc :as jdbc]
   [next.jdbc.connection :as connection]
   [next.jdbc.result-set :as rs]
   [arcaneflare.env :as env])
  (:import (com.zaxxer.hikari HikariDataSource)))

(def spec
  (get (env/config) :pg))

(declare ds)

(m/defstate ^:dynamic ds
  :start (connection/->pool
          HikariDataSource spec)
  :stop (.close ds))

(defn exc [sqls]
  (jdbc/execute!
   ds sqls
   {:builder-fn
    rs/as-kebab-maps}))

(def code
  honey.sql/format)

(def run
  (comp exc honey.sql/format))

(defn csv [path]
  (with-open [rdr (io/reader path)]
    (let [[headers & rows] (csv/read-csv rdr)
          keys (map keyword headers)]
      (->> rows
           (map #(zipmap keys %))
           vec))))

(comment
  (code {:select [:*] :from :member})
  :=> ["SELECT * FROM member"]
  (exc ["select 1 as abc"])
  (exc ["select * from place_love limit 1"])
  :=> [#:place-love{:member-id #uuid "273e92d4-8134-400e-907d-469227f5dc6e",
                    :place-id #uuid "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                    :loved-at #inst "2025-05-20T13:52:03.291361000-00:00"}]
  (run {:select [:*]
        :from :member}))
