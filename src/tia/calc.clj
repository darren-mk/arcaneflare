(ns tia.calc
  "pure functions for domain logic."
  (:require
   [clojure.string :as cstr]
   [malli.core :as m]
   [tia.model :as model]))

(defn >s [& ts]
  (->> ts
       (map name)
       (cstr/join " ")))

(defn nsmap->ns [m]
  (-> (dissoc m :xt/id)
      keys first namespace))

(def ns->idk
  #(keyword (str % "/id")))

(def nsmap->idk
  (comp ns->idk nsmap->ns))

(def ns->schema
  #(->> % (str "tia.model/")
        symbol eval))

(defn session-stringify [id]
  (str "session-id=" id ";path=/"))

(defn letter-or-digit? [c]
  (Character/isLetterOrDigit c))

(defn white-space? [c]
  (Character/isWhitespace c))

(defn remove-special-chars [s]
  (->> (filter #(or (letter-or-digit? %)
                    (white-space? %)) s)
       (apply str)))

(def trim-low
  (comp cstr/trim cstr/lower-case))

(m/=> handlify
      [:=> [:cat :map :string]
       :keyword])

(defn handlify
  [{:keys [address/state address/city]} label]
  (let [named (as-> label $
                (trim-low $)
                (remove-special-chars $)
                (cstr/split $ #" ")
                (remove #(cstr/blank? %) $)
                (cstr/join "-" $))
        expanded (if (< (count named) 12)
                   (cstr/join "-" [(trim-low state)
                                   (trim-low city)
                                   named])
                   named)]
    (keyword expanded)))

(def day->num
  {:mon 0
   :tue 1
   :wed 2
   :thu 3
   :fri 4
   :sat 5
   :sun 6})

(m/=> find-period-f
      [:=> [:cat :keyword] fn?])

(defn find-period-f [day]
  (fn [google-periods]
    (->> google-periods
         (filter #(= (day->num day)
                     (-> % :open :day)))
         first)))

(m/=> idify
      [:=> [:cat :string] :string])

(defn idify [s]
  (->> s
       (remove (fn [c] (= c \ )))
       (apply str)
       cstr/lower-case))

(defn path [& elems]
  (->> elems
       (map #(if (keyword? %) (name %) %))
       (cstr/join "/")
       (str "/")))

(defn split-to-kws [s]
  (as-> s $
    (cstr/split $ #" ")
    (mapv keyword $)))

(comment
  (split-to-kws
   "M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314")
   :=> [:M8 :1.314C12.438-3.248 :23.534 :4.735 :8
        :15-7.534 :4.736 :3.562-3.248 :8 :1.314])
