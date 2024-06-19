(ns tia.handler
  (:require 
   [tia.layout :refer [error-page]]
   [tia.routes :refer [routes]]
   [reitit.ring :as ring]
   [mount.core :as mount]))

(declare app-routes)

#_
(defn- async-aware-default-handler
  ([_] nil)
  ([_ respond _] (respond nil)))

(mount/defstate app-routes
  :start
  (ring/ring-handler
   (ring/router
    [(routes)])
   (ring/routes
    (ring/create-resource-handler
     {:path "/"})
    (ring/create-default-handler
     {:not-found
      (constantly (error-page {:status 404, :title "404 - Page not found"}))
      :method-not-allowed
      (constantly (error-page {:status 405, :title "405 - Not allowed"}))
      :not-acceptable
      (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  #'app-routes)
