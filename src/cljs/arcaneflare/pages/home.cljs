(ns arcaneflare.pages.home)

(defn node []
  [:div
   [:button.button.is-primary
    {:on-click #(js/alert "hello world")}
    "yay, Bulma"]
   [:p "home page"]])