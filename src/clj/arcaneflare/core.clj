(ns arcaneflare.core
  (:require
   [mount.core :as m]
   [clojure.java.io :as io]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]
   [ring.middleware.defaults :as rmd]
   [arcaneflare.middleware :as mw]))

(defn ping [_req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "pong"})

(defn echo [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str req)})

(defn frontend [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> "public/index.html"
             io/resource slurp)})

(defn hello [s]
  {:msg (str "hello, " s)})

(def m
  {:api/hello hello})

(defn api [fk & args]
  (apply (get m fk) args))

(defn tunnel [{:keys [body]}]
  (let [body' (-> body read-string
                  last read-string)]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (apply api body')}))

(def router
  (rmd/wrap-defaults
   (rr/ring-handler
    (rr/router
     [["/" {:get frontend}]
      ["/api" {:middleware [mw/capture-req-body
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
