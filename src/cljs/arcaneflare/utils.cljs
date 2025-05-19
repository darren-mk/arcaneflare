(ns arcaneflare.utils)

(defn reset-tv! [obj]
  (fn [evt]
    (let [v (-> evt .-target .-value)]
      (reset! obj v))))