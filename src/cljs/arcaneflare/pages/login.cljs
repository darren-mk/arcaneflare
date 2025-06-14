(ns arcaneflare.pages.login
  (:require
   [reagent.core :as r]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.http :refer [tunnel]]
   [arcaneflare.token :as tk]
   [arcaneflare.utils :refer [reset-tv!]]))

(defonce username-typed
  (r/atom nil))

(defonce passcode-typed
  (r/atom nil))

(defn username []
  [:<>
   [:label {:for "username"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Your username"]
   [:input {:type "text" :name "text" :id "username"
            :placeholder "type your username"
            :required true :value @username-typed
            :on-change (reset-tv! username-typed)
            :class ["bg-gray-50" "border" "border-gray-300"
                    "text-gray-900" "text-sm" "rounded-lg"
                    "focus:ring-blue-500" "focus:border-blue-500"
                    "block" "w-full" "p-2.5"
                    "dark:bg-gray-600" "dark:border-gray-500"
                    "dark:placeholder-gray-400" "dark:text-white"]}]])

(defn passcode []
  [:<>
   [:label {:for "password"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Your passcode"]
   [:input {:type "password" :name "passcode"
            :id "passcode" :placeholder "••••••••"
            :required true :value @passcode-typed
            :on-change (reset-tv! passcode-typed)
            :class ["bg-gray-50" "border" "border-gray-300"
                    "text-gray-900" "text-sm" "rounded-lg"
                    "focus:ring-blue-500" "focus:border-blue-500"
                    "block" "w-full" "p-2.5"
                    "dark:bg-gray-600" "dark:border-gray-500"
                    "dark:placeholder-gray-400" "dark:text-white"]}]])

(defn submit-fn []
  (tunnel [:api.public.member.root/login!
           {:member/username @username-typed
            :member/passcode @passcode-typed}]
          (fn [token]
            (when token (tk/new token)
                  (rtfe/push-state :route/home)))
          (fn [msg] (println msg))))

(defn title []
  [:h5 {:class ["font-medium" "text-gray-900"
                "text-xl" "dark:text-white"]}
   "Sign in to our platform"])

(defn remember-me []
  [:div {:class ["flex" "items-start"]}
    [:div {:class ["flex" "items-center" "h-5"]}
     [:input {:id "remember" :type "checkbox" :required true
              :class ["w-4" "h-4" "border" "border-gray-300" "rounded-sm"
                      "bg-gray-50" "focus:ring-3" "focus:ring-blue-300"
                      "dark:bg-gray-700" "dark:border-gray-600"
                      "dark:focus:ring-blue-600" "dark:ring-offset-gray-800"
                      "dark:focus:ring-offset-gray-800"]}]]
    [:label {:for "remember"
             :class ["ms-2" "text-sm" "font-medium" "text-gray-900"
                     "dark:text-gray-300"]}
     "Remember me"]])

(defn lost-passcode []
  [:a {:href "#" ;; TODO
       :class ["ms-auto" "text-sm" "text-blue-700" "hover:underline"
               "dark:text-blue-500"]}
   "Lost Passcode?"])

(defn remedy []
  [:div {:class ["flex" "items-start"]}
   [remember-me]
   [lost-passcode]])

(defn submit []
  [:button {:type "submit"
            :class ["w-full" "text-white" "bg-blue-700"
                    "hover:bg-blue-800" "focus:ring-4"
                    "focus:outline-none" "focus:ring-blue-300"
                    "font-medium" "rounded-lg" "text-sm"
                    "px-5" "py-2.5" "text-center"
                    "dark:bg-blue-600" "dark:hover:bg-blue-700"
                    "dark:focus:ring-blue-800"]
    :on-click submit-fn}
   "Login to your account"])

(defn sign-up-link []
  [:div {:class ["flex" "flex-row" "items-center"
                 "justify-between"]}
   [:div {:class ["font-medium" "text-gray-500"
                  "text-sm" "dark:text-gray-300"]}
    "Not registered?"]
   [:a {:href "/#/signup"
        :class ["text-blue-700" "hover:underline"
                "dark:text-blue-500"]}
    "Create account"]])

(defn form []
  [:div {:class ["w-full" "max-w-sm" "p-4" "bg-white"
                 "border" "border-gray-200" "rounded-lg"
                 "shadow-sm" "sm:p-6" "md:p-8"
                 "dark:bg-gray-800" "dark:border-gray-700"]}
   [:form {:class ["space-y-6"]}
    [title]
    [username]
    [passcode]
    [remedy]
    [submit]
    [sign-up-link]]])

(defn node []
  [:div {:class ["flex" "items-center"
                 "justify-center" "min-h-screen"]}
   [form]])
