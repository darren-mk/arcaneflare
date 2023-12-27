(ns tia.layout
  (:require
   [clojure.java.io]
   [hiccup2.core :as h]
   [selmer.parser :as parser]
   [ring.util.http-response :refer [content-type ok]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
   [ring.util.response]
   [tia.components.navbar :refer [navbar]]))

(parser/set-resource-path!
 (clojure.java.io/resource "html"))

(parser/add-tag!
 :csrf-field
 (fn [_ _] (anti-forgery-field)))

(defn plain
  [text]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body text})

(defn html [data]
  (let [bulma "https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css"
        head [:head
              [:link {:rel :stylesheet
                      :href bulma}]
              [:link {:rel :stylesheet
                      :type "text/css"
                      :href "/css"}]]
        body [:body (navbar) data]
        html [:html head body]]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str (h/html html))}))

(defn css [s]
  {:status 200
   :headers {"Content-Type" "text/css"}
   :body s})

(defn render
  "renders the HTML template located relative to resources/html"
  [_request template & [params]]
  (content-type
   (ok
    (parser/render-file
     template
     (assoc params
            :page template
            :csrf-token *anti-forgery-token*)))
   "text/html; charset=utf-8"))

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
