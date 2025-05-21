(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.middleware :as mw]
   [arcaneflare.database.place :as db.place]
   [arcaneflare.database.member :as db.member]
   [arcaneflare.token :as token]
   [clojure.string :as str]))

(defn okay [text-type body]
  {:status 200
   :headers {"Content-Type"
             (case text-type
               :plain "text/plain"
               :html "text/html")}
   :body body})

(defn ping [_req]
  (okay :plain "pong"))

(defn echo [req]
  (okay :plain (str req)))

(defn frontend [_]
  (okay :html
        (-> "public/index.html"
            io/resource slurp)))

(defn hello [s]
  {:msg (str "hello, " s)})

(def tmap
  {:api.public.test/hello hello
   :api.private.place/insert! db.place/insert!
   :api.private.place/upsert! db.place/upsert!
   :api.public.place/single-by db.place/single-by
   :api.public.place/full-list db.place/full-list
   :api.private.place/love! db.place/love!
   :api.private.place/unlove! db.place/unlove!
   :api.public.member/insert! db.member/insert!
   :api.public.member/member-by db.member/member-by
   :api.public.member/login! db.member/login!})

(defn public-api? [k]
  (-> k namespace
      (str/starts-with? "api.public")))

(defn api
  ([k] (api k [] nil))
  ([k args] (api k args nil))
  ([k args token]
   (let [public? (public-api? k)
         f (get tmap k)]
     (cond public? (apply f args)
           (and token (token/verify token))
           (apply f args)))))

(defn tunnel [{:keys [body]}]
  (let [body' (-> body read-string
                  last read-string)]
    (okay :plain (apply api body'))))

(def router
  (rmd/wrap-defaults
   (rr/ring-handler
    (rr/router
     [["/" {:get frontend}]
      ["/api" {:middleware [mw/wrap-cors
                            mw/capture-req-body
                            mw/stringify-resp-body]}
       ["/tunnel" {:post tunnel}]
       ["/ping" {:get ping}]
       ["/echo" {:get echo}]]])
    (rr/routes
     (rr/create-resource-handler {:path "/"})
     (rr/create-default-handler)))
   (-> rmd/site-defaults
       (assoc-in [:security :anti-forgery] false))))

(declare server)

(m/defstate server
  :start (rj/run-jetty
          router
          {:port 3000 :join? false})
  :stop (.stop server))

(defn -main []
  (m/start))
