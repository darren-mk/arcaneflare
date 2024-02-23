(ns tia.pages.post
  (:require
   [tia.components.inputs :as comp-input]
   [tia.data :as d]
   [tia.layout :as l]))

(defn page [_req]
  (l/frame
   {:nav {:selection nil}}
   (comp-input/root {})))

(def routes
  [(:post d/uri)
   ["" {:get page}]])
