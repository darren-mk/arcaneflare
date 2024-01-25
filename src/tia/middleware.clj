(ns tia.middleware
  (:require
   [tia.env :refer [defaults]]
   [tia.db.session :as session-db]
   [clojure.tools.logging :as log]
   [tia.calc :as calc]
   [tia.layout :refer [error-page]]
   [tia.util :as u]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [muuntaja.middleware :refer [wrap-format wrap-params]]
   [luminus-transit.time :as time]
   [muuntaja.core :as m]
   [ring.middleware.flash :refer [wrap-flash]]
   [ring.adapter.undertow.middleware.session :refer [wrap-session]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [tia.data :as d]))

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
    (let [sid (-> req :cookies (get "session-id") :value)
          {:keys [session/renewal
                  session/expiration] :as session}
          (when sid (-> sid parse-uuid session-db/find-by-id))
          person-id (get session :session/person.id)
          expired? (boolean (and expiration (u/past? expiration)))
          renewing? (boolean (and renewal (u/past? renewal)))
          req' (if expired? req
                   (assoc req :session session))
          resp (handler req')]
      (if (and (not expired?) renewing?)
        (let [{:keys [session/id]} (session-db/login! person-id)
              path [:headers d/set-cookie]
              session-str (calc/session-str id)]
          (assoc-in resp path session-str))
        resp))))
