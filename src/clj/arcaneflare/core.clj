(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.middleware :as mw]
   [arcaneflare.database.geo.root :as db.geo.root]
   [arcaneflare.database.place.root :as db.place.root]
   [arcaneflare.database.place.love :as db.place.love]
   [arcaneflare.database.place.vote :as db.place.vote]
   [arcaneflare.database.place.thumbnail :as db.place.thumbnail]
   [arcaneflare.database.member.root :as db.member.root]
   [arcaneflare.database.member.performer :as db.member.performer]
   [arcaneflare.token :as token]
   [arcaneflare.utils :as u]
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

(defn hello [{:keys [hello/first-name]}]
  {:msg (str "hello, " first-name)})

(def fnk-map
  {:api.public.test/hello hello
   :api.public.geo/countries db.geo.root/countries
   :api.public.geo/find-children db.geo.root/find-children
   :api.public.geo/multi-by db.geo.root/multi-by
   :api.private.place/upsert! db.place.root/upsert!
   :api.public.place/single-by db.place.root/single-by
   :api.public.place/multi-by-geo-ids db.place.root/multi-by-geo-ids
   :api.public.place/multi-by-ids db.place.root/multi-by-ids
   :api.public.place/multi-by-fraction db.place.root/multi-by-fraction
   :api.public.place/full-list db.place.root/full-list
   :api.private.place.love/yes! db.place.love/yes!
   :api.private.place.love/no! db.place.love/no!
   :api.private.place.love/how db.place.love/how
   :api.private.place.love/by-member db.place.love/by-member
   :api.private.place.vote/make! db.place.vote/make!
   :api.private.place.vote/remove! db.place.vote/remove!
   :api.private.place.vote/by-place db.place.vote/by-place
   :api.private.place.thumbnail/add! db.place.thumbnail/add!
   :api.public.place.thumbnail/get-by db.place.thumbnail/get-by
   :api.private.place.thumbnail/remove! db.place.thumbnail/remove!
   :api.public.member.root/insert! db.member.root/insert!
   :api.public.member.root/member-by db.member.root/get-by
   :api.public.member.root/login! db.member.root/login!
   :api.private.member.performer/upsert! db.member.performer/upsert!
   :api.private.member.performer/remove! db.member.performer/remove!
   :api.private.member.performer/get-by db.member.performer/get-by})

(defn public-api? [fnk]
  (-> fnk namespace
      (str/starts-with? "api.public")))

(defn token->member-info
  [{:keys [:member/token] :as args}]
  (when-not token
    (throw (ex-info "token not present"
                    {:cause "token missing"})))
  (let [claims (token/verify token)]
    (merge (dissoc args :member/token)
           (dissoc claims :iat :exp))))

(defn api
  ([fnk] (api fnk {}))
  ([fnk args]
   (let [public? (public-api? fnk)
         f (u/fail-nil
            (get fnk-map fnk)
            "api not found")
         args' (if public? args
                   (token->member-info args))]
     (f args'))))

(defn revolve [vecs]
  (if (keyword? (first vecs))
    (apply api vecs)
    (vec (apply concat (map revolve vecs)))))

(defn tunnel [{:keys [body]}]
  (let [body' (-> body read-string
                  last read-string)]
    (okay :plain (revolve body'))))

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
