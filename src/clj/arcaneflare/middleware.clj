(ns arcaneflare.middleware
  (:require
   [ring.middleware.cors :as cors]
   [ring.util.request :as rur]
   #_[clojure.tools.logging :as log]
   #_[arcaneflare.layout :refer [error-page]]
   #_[muuntaja.middleware :refer [wrap-format wrap-params]]
   #_[luminus-transit.time :as time]
   #_[muuntaja.core :as m]
   #_[ring.middleware.flash :refer [wrap-flash]]
   #_[ring.adapter.undertow.middleware.session :refer [wrap-session]]
   #_[ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

#_
(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (error-page
     {:status 403
      :title "Invalid anti-forgery token"})}))

#_
(def instance
  (m/create
   (-> m/default-options
       (update-in
        [:formats "application/transit+json" :decoder-opts]
        (pararcaneflarel merge time/time-deserialization-handlers))
       (update-in
        [:formats "application/transit+json" :encoder-opts]
        (pararcaneflarel merge time/time-serialization-handlers)))))

#_
(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format instance))]
    (fn
      ([request]
       ;; disable wrap-formats for websockets
       ;; since they're not compatible with this middleware
       ((if (:websocket? request) handler wrapped) request))
      ([request respond raise]
       ((if (:websocket? request) handler wrapped) request respond raise)))))

#_
(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-flash
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] false)
           (dissoc :session)))
      wrap-internal-error))

(defn wrap-cors [handler]
  (cors/wrap-cors
   handler
   :access-control-allow-origin [#".*"]
   :access-control-allow-methods
   [:get :put :post :delete :options]))

(defn capture-req-body [handler]
  (fn [req]
    (let [body' (try (-> req
                         rur/body-string)
                     (catch Exception _))]
      (if body'
        (handler (assoc req :body body'))
        (handler req)))))

(defn stringify-resp-body [handler]
  (fn [req]
    (let [{:keys [body] :as resp} (handler req)]
      (if body
        (assoc resp :body (str body))
        resp))))

#_
(defn handle-exception [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        {:status 400
         :headers {"Content-Type" "text/plain"}
         :body (str "server error: "
                    (.getMessage e))}))))
