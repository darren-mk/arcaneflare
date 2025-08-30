(ns arcaneflare.pages.signup
  (:require
   [clojure.string :as cstr]
   [reagent.core :as r]
   [reitit.frontend.easy :as rtfe]
   [arcaneflare.http :refer [tunnel]]
   [arcaneflare.utils :refer [reset-tv!]]))

(defonce username-typed
  (r/atom nil))

(defonce email-typed
  (r/atom nil))

(defonce role-selected
  (r/atom "customer"))

(defonce passcode-typed
  (r/atom nil))

(defn title []
  [:h5 {:class ["font-medium" "text-gray-900"
                "text-xl" "dark:text-white"]}
   "Create your account"])

(defn username []
  [:<>
   [:label {:for "username"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Your desired username"]
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

(defn email []
  [:<>
   [:label {:for "email"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Your email"]
   [:input {:type "email" :name "email" :id "email"
            :placeholder "type your email"
            :required true :value @email-typed
            :on-change (reset-tv! email-typed)
            :class ["bg-gray-50" "border" "border-gray-300"
                    "text-gray-900" "text-sm" "rounded-lg"
                    "focus:ring-blue-500" "focus:border-blue-500"
                    "block" "w-full" "p-2.5"
                    "dark:bg-gray-600" "dark:border-gray-500"
                    "dark:placeholder-gray-400" "dark:text-white"]}]])

(defn role []
  [:<>
   [:label {:for "countries"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Select account category"]
   [:select {:id "role" :value @role-selected
             :on-change (reset-tv! role-selected)
             :class ["bg-gray-50" "border" "border-gray-300"
                     "text-gray-900" "text-sm" "rounded-lg"
                     "focus:ring-blue-500" "focus:border-blue-500"
                     "block" "w-full" "p-2.5"
                     "dark:bg-gray-700" "dark:border-gray-600"
                     "dark:placeholder-gray-400" "dark:text-white"
                     "dark:focus:ring-blue-500"
                     "dark:focus:border-blue-500"]}
    (for [item [:customer :performer :staff :admin]]
      ^{:key [item]}
      [:option {:value item}
       (cstr/capitalize (name item))])]])

(defn passcode []
  [:<>
   [:label {:for "password"
            :class ["block" "mb-2" "text-sm" "font-medium"
                    "text-gray-900" "dark:text-white"]}
    "Your desired passcode"]
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

(defn login-link []
  [:div {:class ["flex" "flex-row" "items-center"
                 "justify-between"]}
   [:div {:class ["font-medium" "text-gray-500"
                  "text-sm" "dark:text-gray-300"]}
    "Already have an account?"]
   [:a {:href "/#/login"
        :class ["text-blue-700" "hover:underline"
                "dark:text-blue-500"]}
    "Log in account"]])

(defn submit-fn []
  (tunnel [:api.public.member.root/insert!
           #:member{:username @username-typed
                    :email @email-typed
                    :passcode @passcode-typed
                    :role @role-selected}]
          (fn [_resp] (rtfe/push-state :route/login))
          (fn [resp] (js/alert resp))))

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
   "Create your account"])

(defn form []
  [:div {:class ["w-full" "max-w-sm" "p-4" "bg-white"
                 "border" "border-gray-200" "rounded-lg"
                 "shadow-sm" "sm:p-6" "md:p-8"
                 "dark:bg-gray-800" "dark:border-gray-700"]}
   [:div {:class ["space-y-6"]}
    [title] [username] [role] [email]
    [passcode] [submit] [login-link]]])

(defn node []
  [:div {:class ["flex" "items-center"
                 "justify-center" "min-h-screen"]}
   [form]])
