(ns arcaneflare.token
  (:require
   [buddy.sign.jwt :as jwt]
   [arcaneflare.env :as env]))

(def secret
  (get-in
   (env/config)
   [:jwt :secret]))

(defn gen! [{:keys [member/id member/role] :as payload}]
  (let [now (quot (System/currentTimeMillis) 1000)
        buffer (* 100 10) ;; update
        exp (+ now buffer)]
    (when-not (and id role)
      (throw (ex-info "id or role not present"
                      {:cause "member id or role missing"})))
    (jwt/sign (assoc payload :iat now :exp exp)
              secret {:alg :hs256})))

(defn unsign [token]
  (try
    (update (jwt/unsign token secret {:alg :hs256})
            :member/id parse-uuid)
    (catch clojure.lang.ExceptionInfo e
      (let [data (ex-data e)]
        (when-not (= (:cause data) :token-expired)
          (throw e))
        nil))))
