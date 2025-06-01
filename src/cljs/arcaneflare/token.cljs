(ns arcaneflare.token
  (:require
   [arcaneflare.state :as c.state]
   [arcaneflare.local :as local]))

(defn pull []
  (or @c.state/token
      (local/read :token)))

(defn refresh []
  (reset! c.state/token
          (local/read :token)))

(defn new [token]
  (local/save :token token)
  (reset! c.state/token token))

(defn remove! []
  (local/delete :token)
  (reset! c.state/token nil))
