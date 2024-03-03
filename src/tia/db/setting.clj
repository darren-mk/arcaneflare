(ns tia.db.setting
  (:require
   [tia.db.common :as dbc]))

(defn auth? [s]
  (-> (dbc/pull-by-id :setting)
      :setting/admin-password
      (= s)))

(defn present []
  (-> (dbc/pull-by-id :setting)
      (dissoc :xt/id)
      (dissoc :setting/id)
      (dissoc :setting/admin-password)))

(comment
  (present)
  #:setting{:setting-expiration-days 3})

(defn set-session-expiration-days [n]
  (dbc/upsert!
   {:setting/id :setting
    :setting/session-expiration-days n}))

(comment
  (set-session-expiration-days 4)
  :=> #:xtdb.api{:tx-id 1913,
                 :tx-time #inst "2024-03-03T03:49:40.098-00:00"})

(defn set-admin-password [s]
  (dbc/upsert!
   {:setting/id :setting
    :setting/session-expiration-days 3
    :setting/admin-password s}))
