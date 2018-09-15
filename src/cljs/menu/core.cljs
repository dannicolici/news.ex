(ns menu.core
  (:require [reagent.core :as r]))

(defn menu-app []
  [:table
   [:tr
    [:td
     [:div
      [:a {:href "register"} "Register"]]
     [:div
      [:a {:href "login"} "Login"]]]
    [:td
     "Use the menu items on the left to authenticate and read/post "
     [:a {:href "news"} "The News"]]]])

(defn ^:export start []
  (r/render-component [menu-app]
                      (.getElementById js/document "root")))