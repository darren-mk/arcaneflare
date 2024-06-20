(ns tia.middleware
  (:require
   [tia.db.session :as session-db]
   #_[clojure.tools.logging :as log] 
   [tia.db.place :as db-place]
   #_[tia.layout :refer [error-page]]
   [tia.util :as u]
   [muuntaja.middleware :refer [wrap-format wrap-params]]
   [luminus-transit.time :as time]
   [muuntaja.core :as m]
   #_[ring.middleware.flash :refer [wrap-flash]]
   #_[ring.adapter.undertow.middleware.session :refer [wrap-session]]
   #_[ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

#_
(defn wrap-internal-error [handler]
  (let [error-result (fn [^Throwable t]
                       (log/error t (.getMessage t))
                       (error-page {:status 500
                                    :title "Something very bad has happened!"
                                    :message "We've dispatched a team of highly trained gnomes to take care of the problem."}))]
    (fn wrap-internal-error-fn
      ([req respond _]
       (handler req respond #(respond (error-result %))))
      ([req]
       (try
         (handler req)
         (catch Throwable t
           (error-result t)))))))

#_
(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (error-page
     {:status 403
      :title "Invalid anti-forgery token"})}))

(def instance
  (m/create
   (-> m/default-options
       (update-in
        [:formats "application/transit+json" :decoder-opts]
        (partial merge time/time-deserialization-handlers))
       (update-in
        [:formats "application/transit+json" :encoder-opts]
        (partial merge time/time-serialization-handlers)))))

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

(defn sessionize [handler]
  (fn [req]
    (let [sid (some-> req :cookies (get "session-id") :value parse-uuid)
          cached-session-m (get @session-db/cache sid)]
      (if (= :out cached-session-m)
        (handler req)
        (let [{:keys [session person]}
              (when sid (or (session-db/get-session-and-person sid)
                            cached-session-m))
              expiration (:session/expiration session)
              expired? (boolean (and expiration (u/past? expiration)))
              req' (if expired? req
                       (-> req
                           (assoc :session session)
                           (assoc :person person)))]
          (swap! session-db/cache dissoc sid)
          (handler req'))))))

(defn handle->place+address [handler]
  (fn [{:keys [path-params] :as req}]
    (let [handle (-> path-params :handle keyword)
          {:keys [place address]}
          (db-place/find-place-and-address handle)
          req' (assoc req :place place :address address)]
      (handler req'))))
