(ns tia.pages.admin
  (:require
   [tia.db.setting :as db-setting]
   [tia.layout :as layout]))

(defn page [{:keys [path-params]}]
  (let [attempt (:password path-params)]
    (layout/page
     {}
     [:div
      (if (db-setting/auth? attempt)
        [:p (str (db-setting/present))]
        [:p "wrong attempt"])])))

(def routes
  ["/admin/:password" {:get page}])
