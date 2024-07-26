(ns arcaneflare.page.home
  (:require
   [reagent-mui.material.button :refer [button]]
   [reagent-mui.colors :as colors]
   [reagent-mui.styles :as styles]
   [reagent-mui.material.css-baseline :refer [css-baseline]]
   [reagent-mui.material.stack :refer [stack]]
   [reagent-mui.material.toolbar :refer [toolbar]]
   [reagent-mui.icons.add-box :refer [add-box]]
   [arcaneflare.component.nav :as nav]))

(def custom-theme
  {:palette {:primary colors/yellow
             :secondary colors/green}})

(def classes (let [prefix "rmui-example"]
               {:root (str prefix "-root")
                :button (str prefix "-button")
                :text-field (str prefix "-text-field")}))

(defn custom-styles [{:keys [theme]}]
  (let [spacing (:spacing theme)]
    {(str "&." (:root classes)) {:margin-left (spacing 8)
                                 :align-items :flex-start}
     (str "& ." (:button classes)) {:margin (spacing 1)}
     (str "& ." (:text-field classes)) {:width 200
                                        :margin-left (spacing 1)
                                        :margin-right (spacing 1)}}))

(defn form* [{:keys [class-name]}]
  [stack {:direction "column"
          :spacing 2
          :class [class-name (:root classes)]}
   [toolbar {:disable-gutters true}
    [button {:variant "contained"
             :color "primary"
             :class (:button classes)}
     "Update value property"
     [add-box]]]])

(def form
  (styles/styled
   form* custom-styles))

(defn node []
  [:<>
   [css-baseline]
   [styles/theme-provider
    (styles/create-theme custom-theme)
    [:div
     [nav/node]
     [button {:variant :outlined
              :color :secondary}
      "yo, mui!"]
     [:p "home page"]
     [form]]]])
