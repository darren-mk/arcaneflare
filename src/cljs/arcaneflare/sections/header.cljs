(ns arcaneflare.sections.header
  (:require
   [reitit.frontend.easy :as rfe]
   [reagent.core :as r]
   [arcaneflare.components.icons :as icons]
   [arcaneflare.state :as state]
   [clojure.string :as cstr]
   [arcaneflare.token :as token]))

(defonce member-dropdown-closed?
  (r/atom true))

(defn logo []
  [:button {:on-click #(rfe/push-state :route/home)
            :class ['flex 'items-center 'space-x-3
                    'rtl:space-x-reverse]}
   [:img {:src "https://www.reshot.com/preview-assets/icons/DNB9LCRHEP/brewing-DNB9LCRHEP.svg"
          :class ['h-8]}]
   [:span {:class ['self-center 'text-2xl 'font-semibold
                   'whitespace-nowrap 'dark:text-white]}
    "FunReview"]])

(defn member-btn []
  (let [{:member/keys [photo-url username email]}
        @state/member]
    [:div {:class ['relative 'inline-block 'text-left]}
     [:button {:type "button"
               :on-click #(swap! member-dropdown-closed? not)
               :class ['flex 'text-sm 'bg-gray-800
                       'rounded-full 'md:me-0 'focus:ring-4
                       'focus:ring-gray-300
                       'dark:focus:ring-gray-600]}
      [:span.sr-only "Open user menu"]
      (if photo-url
        [:img {:src photo-url
               :class ['w-8 'h-8 'rounded-full]
               :alt "user photo"}]
        [:div {:class ['w-8 'h-8 'flex 'items-center
                       'justify-center 'rounded-full
                       'bg-gray-200 'text-gray-600
                       'text-xs 'font-medium 'uppercase]}
         (first username)])]
     [:div {:class [(when @member-dropdown-closed? 'hidden)
                    'absolute 'right-0 'mt-3
                    'z-50 'my-4 'text-base 'list-none
                    'bg-white 'divide-y 'divide-gray-100
                    'rounded-lg 'shadow-sm 'dark:bg-gray-700
                    'dark:divide-gray-600]}
      [:div {:class ["px-4" "py-3"]}
       [:span {:class ['block 'text-gray-900
                       'text-sm 'dark:text-white]}
        username]
       [:span {:class ['block 'text-sm 'text-gray-500
                       'truncate 'dark:text-gray-400]}
        email]]
      [:ul {:class ['py-2]}
       (for [[item f] [[:account #(rfe/push-state :route/account)]
                       [:log-out #(do (reset! state/member nil)
                                      (reset! state/token nil)
                                      (token/remove!))]]]
         ^{:key item}
         [:li {:on-click #(do (reset! member-dropdown-closed? true)
                              (f))
               :class ['block 'text-sm 'px-4 'py-2
                       'text-gray-700 'hover:bg-gray-100
                       'dark:hover:bg-gray-600
                       'dark:text-gray-200
                       'dark:hover:text-white]}
          (-> item name cstr/capitalize)])]]]))

(defonce menu-shown-on-mobile?
  (r/atom false))

(defn mobile-menu-toggle-btn []
  [:button {:type "button"
            :on-click #(swap! menu-shown-on-mobile? not)
            :class ['inline-flex 'items-center 'p-2
                    'w-10 'h-10 'justify-center 'text-sm
                    'text-gray-500 'rounded-lg 'md:hidden
                    'hover:bg-gray-100 'focus:outline-none
                    'focus:ring-2 'focus:ring-gray-200
                    'dark:text-gray-400 'dark:hover:bg-gray-700
                    'dark:focus:ring-gray-600]}
   [:span.sr-only "Open main menu"]
   [icons/burger]])

(defn links []
  (let [path (get @state/match :path)]
    [:ul {:class ['flex 'flex-col 'font-medium 'p-4
                  'md:p-0 'mt-4 'border 'border-gray-100
                  'rounded-lg 'bg-gray-50 'md:space-x-8
                  'rtl:space-x-reverse 'md:flex-row
                  'md:mt-0 'md:border-0 'md:bg-white
                  'dark:bg-gray-800 'md:dark:bg-gray-900
                  'dark:border-gray-700]}
     (for [item [:route/places :route/performers :route/threads]]
       ^{:key item}
       (let [selected? (cstr/includes? path (name item))]
         [:li [:button {:on-click #(rfe/push-state item)
                        :class [(if selected? 'text-green-400 'text-white)
                                'block 'py-2 'px-3 'rounded-sm 'text-gray-900
                                'hover:bg-gray-100 'md:hover:bg-transparent
                                'md:hover:text-blue-700
                                'md:dark:hover:text-blue-500 'dark:hover:bg-gray-700
                                'dark:hover:text-white 'md:dark:hover:bg-transparent
                                'dark:border-gray-700]}
               (cstr/capitalize (name item))]]))]))

(defn menu-on-mobile []
  (when @menu-shown-on-mobile?
    [:div {:class ['md:hidden 'md:order-1 'w-full]}
     [links]]))

(defn menu-on-desktop []
  [:div {:class ['hidden 'md:block 'items-center
                 'justify-between 'w-full 'md:flex
                 'md:w-auto 'md:order-1]}
   [links]])

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
     [member-btn]
     [get-started])
   [mobile-menu-toggle-btn]])

(defn navbar []
  [:nav {:class ['bg-white 'border-gray-200
                 'dark:bg-gray-900]}
   [:div {:class ['max-w-screen-xl 'flex 'flex-wrap
                  'items-center 'justify-between
                  'mx-auto 'p-4]}
    [logo] [member]
    [menu-on-desktop]
    [menu-on-mobile]]])
