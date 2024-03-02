(ns tia.pages.logout
  (:require
   [clojure.string :as cstr]
   [tia.layout :as l]
   [tia.db.session :as db-session]))

(defn logout-and-redirect [{:keys [session]}]
  (db-session/logout! (:session/id session))
  (l/page {:})
  )

(def routes
  ["/logout" {:get logout-and-redirect}])
