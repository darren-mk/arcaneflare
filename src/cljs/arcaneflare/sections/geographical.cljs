(ns arcaneflare.sections.geographical
  (:require
   [reagent.core :as r]
   [arcaneflare.components.badges :as badges]
   [arcaneflare.http :as http]
   [arcaneflare.state :as state]
   [arcaneflare.components.icons :as icons]))

(defonce fragment
  (r/atom nil))

(defonce items
  (r/atom nil))

(defn clean []
  (reset! fragment nil)
  (reset! items nil))

(defn pull [fraction]
  (http/tunnel
   [[:api.public.geo/multi-by
     {:geo/fraction fraction}]
    [:api.public.place/multi-by-fraction
     {:place/fraction fraction}]]
   #(reset! items %)
   #(js/alert "?")))

(defn tags [{:keys [reaction]}]
  [:div {:class ['max-w-screen-xl]}
   (for [[id {geo-name :geo/full-name
              place-name :place/name}] @state/geography]
     ^{:key id}
     [badges/large {:color :purple
                    :label (or geo-name place-name)
                    :on-click #(do (swap! state/geography dissoc id)
                                   (when reaction (reaction)))}])])

(defn search-bar [{:keys [_reaction]}]
  [:div {:class ['w-full]}
   [:div {:class ['flex]}

    ;; Label (screen-reader only)
    [:label {:for "search-dropdown"
             :class ['mb-2 'text-sm 'font-medium
                     'text-gray-900 'sr-only
                     'dark:text-white]}
     "Your Email"]

    ;; Dropdown Button
    [:button {:id "dropdown-button"
              :type "button"
              :data-dropdown-toggle "dropdown"
              :class ['shrink-0 'z-10 'inline-flex
                      'items-center 'py-2.5 'px-4
                      'text-sm 'font-medium 'text-center
                      'text-gray-900 'bg-gray-100
                      'border 'border-gray-300
                      'rounded-s-lg 'hover:bg-gray-200
                      'focus:ring-4 'focus:outline-none
                      'focus:ring-gray-100 'dark:bg-gray-700
                      'dark:hover:bg-gray-600
                      'dark:focus:ring-gray-700
                      'dark:text-white 'dark:border-gray-600]}
     "All categories"
     [icons/arrow-down]]

    ;; Dropdown Menu
    [:div {:id "dropdown"
           :class ['z-10 'hidden 'bg-white
                   'divide-y 'divide-gray-100
                   'rounded-lg 'shadow-sm 'w-44
                   'dark:bg-gray-700]}
     [:ul {:class ['py-2 'text-sm 'text-gray-700
                   'dark:text-gray-200]
           :aria-labelledby "dropdown-button"}
      (for [label ["Mockups" "Templates" "Design" "Logos"]]
        ^{:key label}
        [:li [:button {:type "button"
                       :class ['inline-flex 'w-full 'px-4 'py-2
                               'hover:bg-gray-100
                               'dark:hover:bg-gray-600
                               'dark:hover:text-white]}
              label]])]]

    ;; Search input + button
    [:div {:class ['relative 'w-full]}
     [:input {:type "search"
              :id "search-dropdown"
              :required true
              :placeholder "Search States, Cities, or Spots!"
              :value @fragment
              :on-change (fn [e]
                           (reset! fragment (-> e .-target .-value str))
                           (if (and (seq @fragment)
                                    (< 3 (count @fragment)))
                             (pull @fragment)
                             (reset! items nil)))
              :class "block p-2.5 w-full z-20 text-sm text-gray-900
                      bg-gray-50 rounded-e-lg border-s-gray-50
                      border-s-2 border border-gray-300 focus:ring-blue-500
                      focus:border-blue-500 dark:bg-gray-700
                      dark:border-s-gray-700 dark:border-gray-600
                      dark:placeholder-gray-400 dark:text-white
                      dark:focus:border-blue-500"}]]]])

(defn search-result [{:keys [reaction]}]
  [:div {:id "default-modal"
         :tab-index "-1"
         :aria-hidden "true"
         :class [(when-not (and @items (seq @items)) 'hidden)
                 'absolute 'top-50 "left-1/2" 'transform "-translate-x-1/2"
                 'z-30 'flex 'items-center 'justify-center 'w-full 'max-h-full]}
   [:div {:class ['relative 'p-4 'w-full 'max-w-2xl 'max-h-full]}
    [:div {:class ['relative 'bg-white 'rounded-lg 'shadow-sm 'dark:bg-gray-700]}

     ;; Header
     [:div {:class "flex items-center justify-between p-4 md:p-5
                    border-b rounded-t dark:border-gray-600 border-gray-200"}
      [:h3 {:class "text-xl font-semibold text-gray-900 dark:text-white"}
       "Result Findings"]
      [:button {:type "button"
                :on-click #(clean)
                :class "text-gray-400 bg-transparent hover:bg-gray-200
                        hover:text-gray-900 rounded-lg text-sm w-8 h-8
                        ms-auto inline-flex justify-center items-center
                        dark:hover:bg-gray-600 dark:hover:text-white"}
       [icons/close-x]
       [:span.sr-only "Close modal"]]]

     ;; Body
     [:div {:class "p-8 space-y-4"}
      [:div {:class "flex flex-col gap-4"}
       (for [{geo-id :geo/id geo-name :geo/full-name
              geo-kind :geo/kind place-id :place/id
              place-name :place/name :as m} @items]
         ^{:key (or geo-id place-id)}
         [:button {:class "text-base leading-relaxed
                           text-gray-500 dark:text-gray-400"
                   :on-click #(do (swap! state/geography
                                         assoc (or geo-id place-id) m)
                                  (when reaction (reaction)))}
          (str (or geo-name place-name) " ("
               (or geo-kind "club") ")")])]]]]])