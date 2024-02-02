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

(m/=> handlify
      [:=> [:cat :string]
       :keyword])

(defn handlify [s]
  (as-> s $
    (cstr/trim $)
    (cstr/lower-case $)
    (remove-special-chars $)
    (cstr/split $ #" ")
    (remove #(cstr/blank? %) $)
    (cstr/join "-" $)
    (keyword $)))

(m/=> schedulify
      [:=> [:cat vector?]
       model/schedules])

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

(defn schedulify [google-periods]
  (let [gps google-periods]
    (as-> [:mon :tue :wed :thu :fri :sat :sun] $
         (group-by identity $)
         (update-vals $ #((find-period-f (first %)) gps)))))

(defn mask-gplace [fields]
  (->> fields
       (map #(str "places." (name %)))
       (cstr/join ",")))
