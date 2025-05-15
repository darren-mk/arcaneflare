(ns arcaneflare.theme
  (:require
   [arcaneflare.state.controlled :as state]
   [arcaneflare.local :as local]))

(defn locally []
  (-> :theme local/read keyword))

(defn bring []
  (or @state/theme
      (locally)))

(defn tag []
  (.getElementById
   js/document "html"))

(defn fill [k]
  (.setAttribute
   (tag)
   "data-theme"
   (name k)))

(defn ensure []
  (fill (bring)))

(defn toggle []
  (let [current (bring)
        new-theme (case current
                    :light :dark
                    :dark :light)]
    (local/save :theme (name new-theme))
    (reset! state/theme new-theme)
    (fill new-theme)))