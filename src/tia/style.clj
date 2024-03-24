(ns tia.style
  (:require
   [clojure.string :as cstr]
   [garden.core :as garden]
   [tia.layout :as l]))

(def sum
  #:css
  {:big-font {:font-size :64px
              :color :blue}
   :abc {:background-color "red"}})

(defn fmt [[nk v]]
  [(->> nk name (str ".") keyword) v])

(comment
  (apply fmt {:abc/xyz {:color :blue}})
  :=> [:.xyz {:color :blue}])

(defn cl [& nks]
  (let [f (fn [nk]
            (assert (get sum nk))
            (->> nk name))
        l (map f nks)]
    (cstr/join " " l)))

(comment
  (cl :abc/xyz :def/fgh)
  :=> "xyz fgh")

(defn core [_req]
  (->> sum
       (map fmt)
       (apply garden/css)
       l/css))
