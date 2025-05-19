(ns arcaneflare.token
  (:require
   [buddy.sign.jwt :as jwt]
   [arcaneflare.env :as env]))

(def secret
  (get-in
   (env/config)
   [:jwt :secret]))

(defn gen! [claims]
  (let [now (quot (System/currentTimeMillis) 1000)
        buffer (* 100 10) ;; update
        exp (+ now buffer)]
    (jwt/sign (assoc claims
                     :iat now
                     :exp exp)
              secret {:alg :hs256})))

(defn verify [token]
  (try
    (jwt/unsign token secret {:alg :hs256})
    (catch clojure.lang.ExceptionInfo e
      (let [data (ex-data e)]
        (when-not (= (:cause data) :token-expired)
          (throw e))
        nil))))