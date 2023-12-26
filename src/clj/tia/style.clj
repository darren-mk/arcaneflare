(ns tia.style
  (:require
   [clojure.string :as cstr]
   [garden.core :as g]
   [tia.layout :as l]))

(def sum
  {:css/nice {:font-size :64px
              :color :blue}
   :css/abc {:background-color "red"}})

(defn f
  {:eg (comment
         (apply f {:abc/xyz {:color :blue}})
                :=> [:.xyz {:color :blue}])}
  [[nk v]]
  [(->> nk name (str ".") keyword) v])

(defn c
  {:eg (comment
         (c :abc/xyz :def/fgh)
         :=> "xyz fgh")}
  [& nks]
  (let [l (map (fn [nk] (->> nk name)) nks)]
    (cstr/join " " l)))

(defn core [_r]
  (l/css
   (apply
    g/css
    (map f sum))))
