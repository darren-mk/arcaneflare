(ns tia.data)

(def bulma-url
  "https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css")

(def content-type
  (let [k "Content-Type"]
    {:plain {k "text/plain"}
     :css {k "text/css"}
     :html {k "text/html"}}))

(def text-css
  "text/css")
