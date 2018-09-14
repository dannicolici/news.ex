(ns menu.core
  (:require [reagent.core :as r]))

(defn menu-app []
  [:div
   [:a {:href "register"} "Register"]
   [:br]
   [:a {:href "login"} "Login"]
   [:br]
   [:form {:action "logout" :method "post"} [:button "Logout"]]
   [:br]
   [:a {:href "news"} "News"]])

(defn ^:export start []
  (r/render-component [menu-app]
                      (.getElementById js/document "root")))