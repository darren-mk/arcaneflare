(ns arcaneflare.http
  (:require
   [ajax.core :as a]
   [cljs.reader :as r]))

(def base-api-url
  (if goog.DEBUG
    "http://localhost:3000"
    ""))

(defn tunnel
  [[_fk _args :as vec]
   success-fn failure-fn]
  (let [url (str base-api-url
                 "/api/tunnel")
        payload (merge
                 {:params (str vec)
                  :handler #(success-fn (r/read-string %))
                  :error-handler failure-fn})]
    (a/POST url payload)))

(comment
  (tunnel [:api.public.test/hello
           {:hello/first-name "Yoko"}]
          #(println "success:" %)
          #(println "failure:" %)))
