(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.middleware :as mw]
   [arcaneflare.database.place.root :as db.place.root]
   [arcaneflare.database.place.love :as db.place.love]
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

(defn hello [{:keys [first_name]}]
  {:msg (str "hello, " first_name)})

(def fnk-map
  {:api.public.test/hello hello
   :api.private.place/upsert! db.place.root/upsert!
   :api.public.place/single-by db.place.root/single-by
   :api.public.place/full-list db.place.root/full-list
   :api.private.place.love/yes! db.place.love/yes!
   :api.private.place.love/no! db.place.love/no!
   :api.private.place.love/how db.place.love/how
   :api.private.place.love/by-member db.place.love/by-member
   :api.private.place/vote! db.place.root/vote!
   :api.private.place/unvote! db.place.root/unvote!
   :api.private.place/vote-score db.place.root/vote-score
   :api.private.place/add-thumbnail! db.place.root/add-thumbnail!
   :api.public.place/get-thumbnails db.place.root/get-thumbnails
   :api.private.place/remove-thumbnail! db.place.root/remove-thumbnail!
   :api.public.member/insert! db.member/insert!
   :api.public.member/member-by db.member/member-by
   :api.public.member/login! db.member/login!})

(defn private-api? [fnk]
  (-> fnk namespace
      (str/starts-with? "api.private")))

(defn api
  ([fnk] (api fnk [] nil))
  ([fnk args] (api fnk args nil))
  ([fnk args token]
   (let [private? (private-api? fnk)
         f (get fnk-map fnk)]
     (if private?
       (let [claims (token/verify token)
             member-info [:member/id :member/role]
             inclusion (select-keys claims member-info)]
         (f (merge args inclusion)))
       (f args)))))

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
