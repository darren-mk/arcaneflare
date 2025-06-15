(ns arcaneflare.sections.header
  (:require
   [arcaneflare.state :as state]))

(defn logo []
  [:a {:href "/"
       :class ['flex 'items-center 'space-x-3
               'rtl:space-x-reverse]}
   [:img {:src "https://flowbite.com/docs/images/logo.svg"
          :class "h-8"
          :alt "Flowbite Logo"}]
   [:span {:class ["self-center" "text-2xl" "font-semibold"
                   "whitespace-nowrap" "dark:text-white"]}
    "Flowbite"]])

(def member-dropdown-id
  "member-dropdown")

(defn member-drop-down-btn []
  (let [{:member/keys [photo-url username]} @state/member]
    [:button {:type "button"
              :id "user-menu-button"
              :aria-expanded "false"
              :data-dropdown-toggle member-dropdown-id
              :data-dropdown-placement "bottom"
              :class ['flex 'text-sm 'bg-gray-800
                      'rounded-full 'md:me-0 'focus:ring-4
                      'focus:ring-gray-300
                      'dark:focus:ring-gray-600]}
     [:span.sr-only "Open user menu"]
     (if photo-url
       [:img {:src photo-url :class "w-8 h-8 rounded-full" :alt "user photo"}]
       [:div {:class ['w-8 'h-8 'flex 'items-center
                      'justify-center 'rounded-full
                      'bg-gray-200 'text-gray-600
                      'text-xs 'font-medium 'uppercase]}
        (first username)])]))

(defn member-drop-down-menu []
  [:div {:id member-dropdown-id
         :class ['z-50 'hidden 'my-4 'text-base 'list-none 'bg-white
                 'divide-y 'divide-gray-100 'rounded-lg 'shadow-sm
                 'dark:bg-gray-700 'dark:divide-gray-600]}
   [:div {:class ["px-4" "py-3"]}
    [:span {:class ["block" "text-sm" "text-gray-900"
                    "dark:text-white"]}
     "Bonnie Green"]
    [:span {:class ["block" "text-sm" "text-gray-500"
                    "truncate" "dark:text-gray-400"]}
     "name@flowbite.com"]]
   [:ul {:class ["py-2"]
         :aria-labelledby "user-menu-button"}
    (for [[label href] [["Dashboard" "#"]
                        ["Settings" "#"]
                        ["Earnings" "#"]
                        ["Sign out" "#"]]]
      [:li [:a {:href href
                :class ['block 'px-4 'py-2 'text-sm 'text-gray-700
                        'hover:bg-gray-100 'dark:hover:bg-gray-600
                        'dark:text-gray-200 'dark:hover:text-white]}
            label]])]])

(defn mobile-menu-toggle-btn []
  [:button {:type "button"
            :data-collapse-toggle "navbar-user"
            :aria-controls "navbar-user"
            :aria-expanded "false"
            :class ['inline-flex 'items-center 'p-2 'w-10 'h-10
                    'justify-center 'text-sm 'text-gray-500 'rounded-lg
                    'md:hidden 'hover:bg-gray-100 'focus:outline-none
                    'focus:ring-2 'focus:ring-gray-200
                    'dark:text-gray-400 'dark:hover:bg-gray-700
                    'dark:focus:ring-gray-600]}
   [:span.sr-only "Open main menu"]
   [:svg
    {:class "w-5 h-5"
     :aria-hidden "true"
     :xmlns "http://www.w3.org/2000/svg"
     :fill "none"
     :viewBox "0 0 17 14"}
    [:path
     {:stroke "currentColor"
      :stroke-linecap "round"
      :stroke-linejoin "round"
      :stroke-width "2"
      :d "M1 1h15M1 7h15M1 13h15"}]]])

(defn links []
  [:div {:id "navbar-user"
         :class ['items-center 'justify-between 'hidden
                 'w-full 'md:flex 'md:w-auto 'md:order-1]}
   [:ul {:class ['flex 'flex-col 'font-medium 'p-4 'md:p-0 'mt-4
                 'border 'border-gray-100 'rounded-lg 'bg-gray-50
                 'md:space-x-8 'rtl:space-x-reverse 'md:flex-row
                 'md:mt-0 'md:border-0 'md:bg-white
                 'dark:bg-gray-800 'md:dark:bg-gray-900
                 'dark:border-gray-700]}
    (for [[label href] [["Places" "/#/places"]
                        ["Performers" "/#/performers"]
                        ["Threads" "/#/threads"]]]
      [:li [:a {:href href
                :aria-current (when (= label "Home") "page")
                :class ['block 'py-2 'px-3 'rounded-sm 'text-gray-900
                        'hover:bg-gray-100 'md:hover:bg-transparent
                        'md:hover:text-blue-700 'dark:text-white
                        'md:dark:hover:text-blue-500 'dark:hover:bg-gray-700
                        'dark:hover:text-white 'md:dark:hover:bg-transparent
                        'dark:border-gray-700]}
            label]])]])

(defn get-started []
  [:a {:type "button"
       :href "/#/login"
       :class ['text-white 'bg-blue-700 'hover:bg-blue-800
               'focus:ring-4 'focus:outline-none 'focus:ring-blue-300
               'font-medium 'rounded-lg 'text-sm 'px-4 'py-2
               'text-center 'dark:bg-blue-600 'dark:hover:bg-blue-700
               'dark:focus:ring-blue-800]}
   "Get started"])

(defn member []
  [:div {:class ['flex 'items-center 'md:order-2
                 'space-x-3 'md:space-x-0
                 'rtl:space-x-reverse]}
   (if @state/member
     [:<>
      [member-drop-down-btn]
      [member-drop-down-menu]
      [mobile-menu-toggle-btn]]
     [get-started])])

(defn navbar []
  [:nav {:class ['bg-white 'border-gray-200
                 'dark:bg-gray-900]}
   [:div {:class ['max-w-screen-xl 'flex 'flex-wrap
                  'items-center 'justify-between
                  'mx-auto 'p-4]}
    [logo] [member] [links]]])
