(ns tia.pages.logout
  (:require
   [tia.layout :as l]
   [tia.db.session :as db-session]))

(defn logout-and-redirect [{:keys [session]}]
  (db-session/delete! (:session/id session))
  (l/redirect "/"))

(def routes
  ["/logout" {:get logout-and-redirect}])
