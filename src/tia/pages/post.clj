(ns tia.pages.post
  (:require
   [tia.components.post :as comp-post]
   [tia.data :as d]
   [tia.layout :as l]))

(defn page [_req]
  (l/frame
   {:nav {:selection nil}}
   (comp-post/root {})))

(def routes
  [(:post d/uri)
   ["" {:get page}]])
