(ns tia.layout
  (:require
   [clojure.java.io]
   [hiccup2.core :as h]
   [selmer.parser :as parser]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.response]
   [tia.data :as d]
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

(defn frag [data]
  {:status 200
   :headers (:html d/content-type)
   :body (-> data h/html str)})

(defn frame [prop data]
  (let [head [:head
              d/htmx d/ui-style
              d/theme-style
              d/css-link d/icons]
        body [:body.d-flex.flex-column.min-vh-100
              (navbar (-> prop :nav :selection))
              data d/ui-action]
        html [:html d/html-prop
              head body]]
    {:status 200
     :headers (:html d/content-type)
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
