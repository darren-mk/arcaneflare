(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]

   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.middleware :as mw]
   [arcaneflare.database.place :as db.place]
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
   :api.private.place/insert db.place/insert!
   :api.private.place/upsert db.place/upsert!
   :api.public.place/get-single-by db.place/get-single-by})

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
           token (apply f args)))))

(defn tunnel [{:keys [body]}]
  (println "@@@@ body" body)
  (let [body' (-> body read-string
                  last read-string)]
    (println "@@@@ body'" body')
    (okay :plain (apply api body'))))

(apply api  [:api.public.test/hello ["kim"]])
(apply api
       [:api.private.place/upsert
        [{:address "641 W 51 st St New York NY 10019"
          :name " Hustler!!!", :city "New York",
          :county "New York", :state "NY",
          :zipcode "10019", :region "Northeast",
          :id #uuid "aa19dfc4-f9ab-4fa4-8f1e-0e879d65fc98"
          :lon -73.9946, :handle "aa19dfc4-hustler-club-new-york",
          :lat 40.7679, :country "USA",
          :district "Manhattan"}]
        "sometoken"])


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

(defn ^:export run [_]
  (m/start))
