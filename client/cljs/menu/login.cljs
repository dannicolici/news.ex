(ns menu.login
  (:require [reagent.core :as r]))

(defn login-app []
  [:div
   [:form {:action "login" :method "post"}
    [:input {:type "text" :name "username"}]
    [:input {:type "password" :name "password"}]
    [:button "Login"]
    [:br] [:a {:href "/"} " << Back to menu"]
    (if (clojure.string/includes?
          (str (-> js/window .-location .-search))
          "login_failed=Y")
     [:div "Login failed! Try again."])]])


(defn ^:export start []
  (r/render-component [login-app]
                      (.getElementById js/document "root")))