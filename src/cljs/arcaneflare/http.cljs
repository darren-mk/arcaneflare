(ns arcaneflare.http
  (:require
   [ajax.core :as a]
   [cljs.reader :as r]))

(defn tunnel [[_fk & _args :as vec]
              handler error-handler]
  (let [payload (merge
                 {:params (str vec)
                  :handler #(handler (r/read-string %))}
                 (when error-handler
                   {:error-handler error-handler}))]
    (a/POST "/api/tunnel" payload)))

(comment
  (tunnel
   [:api/hello "kim"]
   #(js/alert (:msg %))
   identity))
