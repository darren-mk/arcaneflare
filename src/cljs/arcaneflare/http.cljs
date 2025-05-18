(ns arcaneflare.http
  (:require
   [ajax.core :as a]
   [cljs.reader :as r]))

(def base-api-url
  (if goog.DEBUG
    "http://localhost:3000"
    ""))

(defn tunnel [[_fk & _args :as vec]
              handler error-handler]
  (let [url (str base-api-url
                 "/api/tunnel")
        payload (merge
                 {:params (str vec)
                  :handler #(handler (r/read-string %))}
                 (when error-handler
                   {:error-handler error-handler}))]
    (a/POST url payload)))

(comment
  (js/alert 123)
  (tunnel
   [:api.public.test/hello ["kim"]]
   #(js/alert (:msg %))
   identity)
  (tunnel
   [:api.public.place/get-single-by
    [{:handle "d5f1c09d-sapphire-las-vegas"}]]
   #(println %)
   identity)
  (tunnel
   [:api.private.place/upsert 
        [{:address "641 W 51 st St New York NY 10019"
          :name " Hustler!!!", :city "New York", 
          :county "New York", :state "NY", 
          :zipcode "10019", :region "Northeast", 
          :id #uuid "aa19dfc4-f9ab-4fa4-8f1e-0e879d65fc98"
          :lon -73.9946, :handle "aa19dfc4-hustler-club-new-york",
          :lat 40.7679, :country "USA", 
          :district "Manhattan"}] 
        "sometoken"]
   #(println %)
   identity))
