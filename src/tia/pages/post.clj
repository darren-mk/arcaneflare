(ns tia.pages.post
  (:require
   [tia.components.write :as comp-write]
   [tia.data :as d]
   [tia.layout :as l]))

(defn page [_req]
  (l/frame
   {:nav {:selection nil}}
   (comp-write/root {})))

(def routes
  [(:post d/uri)
   ["" {:get page}]])
