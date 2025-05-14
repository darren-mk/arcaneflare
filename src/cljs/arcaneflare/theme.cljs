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

(defn push [k]
  (.setAttribute
   (tag)
   "data-theme"
   (name k)))

(defn coerce []
  (push (bring)))

(defn toggle []
  (let [current (bring)
        new-theme (case current
                    :light :dark
                    :dark :light)]
    (local/save :theme (name new-theme))
    (reset! state/theme new-theme)
    (push new-theme)))