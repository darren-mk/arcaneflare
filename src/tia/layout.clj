(ns tia.layout
  (:require
   [clojure.java.io]
   [hiccup2.core :as h]
   [malli.core :as m]
   [selmer.parser :as parser]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.response]
   [tia.calc :as calc]
   [tia.data :as d]
   [tia.util :as u]
   [tia.components.navbar :refer [navbar]]))

(parser/set-resource-path!
 (clojure.java.io/resource "html"))

(parser/add-tag!
 :csrf-field
 (fn [_ _] (anti-forgery-field)))

(defn plain
  [text]
  {:status 200
   :headers (:plain d/content-type)
   :body text})

(defn comp [data]
  {:status 200
   :headers (:html d/content-type)
   :body (-> data h/html str)})

(m/=> headerize
      [:=> [:cat [:maybe :uuid]] :map])

(defn headerize [session-id]
  (merge
   (:html d/content-type)
   (when session-id
     {d/set-cookie
      (calc/session-stringify
       session-id)})))

(comment
  (headerize (u/uuid))
  :=> {"Content-Type" "text/html",
       "Set-Cookie" "session-id=2dfd...;path=/"}
  (headerize nil)
  :=> {"Content-Type" "text/html"})

(defn bodify [prop data]
  [:body.d-flex.flex-column.min-vh-100
   (navbar prop)
   data d/ui-action])

(defn headify []
  [:head
   d/htmx d/ui-style
   d/css-link d/icons])

(defn page [prop data]
  (let [session-id (-> prop :session :id)
        headers (headerize session-id)
        head (headify)
        body (bodify prop data)
        html [:html d/html-prop
              head body]]
    {:status 200
     :headers headers
     :body (-> html h/html str)}))

(defn css [s]
  {:status 200
   :headers (:css d/content-type)
   :body s})

(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)
   :message - detailed error message (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (parser/render-file "error.html" error-details)})
