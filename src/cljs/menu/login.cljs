(ns menu.login
  (:require [reagent.core :as r]))

(defn login-app []
  [:form {:action "login" :method "post"}
   [:input {:type "text" :name "username"}]
   [:input {:type "password" :name "password"}]
   [:button "Login"]
   [:br][:a {:href "/"} " << Back to menu"]])

(defn ^:export start []
  (r/render-component [login-app]
                      (.getElementById js/document "root")))