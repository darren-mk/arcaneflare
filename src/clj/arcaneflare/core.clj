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

(defn hello [{:keys [first_name]}]
  {:msg (str "hello, " first_name)})

(def fnk-map
  {:api.public.test/hello hello
   :api.private.place/upsert! db.place/upsert!
   :api.public.place/single-by db.place/single-by
   :api.public.place/full-list db.place/full-list
   :api.private.place/love! db.place/love!
   :api.private.place/unlove! db.place/unlove!
   :api.private.place/how-loved db.place/how-loved
   :api.public.place/loved-by-member db.place/loved-by-member
   :api.private.place/vote! db.place/vote!
   :api.private.place/unvote! db.place/unvote!
   :api.private.place/vote-score db.place/vote-score
   :api.private.place/add-thumbnail! db.place/add-thumbnail!
   :api.public.place/get-thumbnails db.place/get-thumbnails
   :api.private.place/remove-thumbnail! db.place/remove-thumbnail!
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
             inclusion (select-keys
                        claims
                        [:member/id :member/role])]
         (f (merge args inclusion)))
       (f args)))))

(comment
  (api :api.public.member/member-by {:member/username "futomaki123"})
  (api :api.public.member/login! #:member{:username "futomaki123"
                                          :passcode "asdf1234!@#$"})
  (def tk
    "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXIvaWQiOiJlYTdmNDA2OC04MmQ1LTQ5ZjktYmRkOC0xYzNjNjczMTFhNWMiLCJtZW1iZXIvcm9sZSI6InN0YWZmIiwiaWF0IjoxNzQ4MDA4MjQwLCJleHAiOjE3NDgwMDkyNDB9.yb_dLuIMgNGHJPmMG5EzQf_jrGeCJXoGICtUlVGTfLo")

  (api :api.public.member/insert! #:member{:id (random-uuid)
                                           :username "futomaki123"
                                           :email "futomaki123@eml.com"
                                           :role "staff"
                                           :passcode "asdf1234!@#$"})

  (api :api.private.place/upsert!
       #:place{:id #uuid "e5f6a7b8-c9d0-1234-ef12-345678901234"
               :name "Sam's Hofbrau"
               :handle "e5f6a7b8-sams-hofbrau"
               :address "1751 E Olympic Blvd, Los Angeles, CA 90021"
               :city "Los Angeles"
               :district "Downtown"
               :state "CA"
               :zipcode "90021"
               :country "USA"
               :county "Los Angeles"
               :region "West"
               :lat 34.0245
               :lon -118.2390
               :phone-number "(213) 747-9444"
               :website-url "http://www.samshofbrau.com/"
               :twitter-url "https://twitter.com/samshofbrau"
               :instagram-url "https://www.instagram.com/samshofbrau/"
               :facebook-url "https://www.facebook.com/samshofbrau"}
       tk)

  (token/verify tk)

  )



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
