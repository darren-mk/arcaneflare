(ns tia.env
  (:require
   [selmer.parser :as parser]
   [clojure.tools.logging :as log]
   [tia.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[tia started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[tia has shut down successfully]=-"))
   :middleware wrap-dev})
