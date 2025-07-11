(ns arcaneflare.components.badges)

(defn large [{:keys [color label on-click]}]
  (let [color' (name color)
        base '[text-sm font-medium me-2 px-8 py-4 rounded-lg]
        dynamic [(str "bg-" color' "-100")
                 (str "text-" color' "-800")
                 (str "dark:bg-" color' "-900")
                 (str "dark:text-" color' "-300")]]
    [:span (merge {:class (vec (concat base dynamic))}
                  (when on-click {:on-click on-click}))
     label]))