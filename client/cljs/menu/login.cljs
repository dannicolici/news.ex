(ns menu.login
  (:require [reagent.core :as r]            
            [reagent.cookies :as cookies]
            [ajax.core :refer [GET POST]]))

(def errorMessage (r/atom ""))

(defn login [id pwd]
  (POST "/api/user/login" {:format :raw
                           :params {:id id :pwd pwd}
                           :handler (fn [r] (let [json (js->clj r)]
                                              (cookies/set! :news_cookie (get json "token"))                                              
                                              (if (contains? json "token") 
                                                (set! (.-location js/document) "/news")
                                                (reset! errorMessage "Login failed! Try again"))))}))
(defn elem [id]
  (.getElementById js/document id))

(defn login-app []
  [:div
   "Username" [:input {:type "text" :name "id" :id "id"}] [:br]
   "Password" [:input {:type "password" :name "pwd" :id "pwd"}][:br]
   [:button {:onClick #(login (.-value (elem "id"))
                              (.-value (elem "pwd")))} "Login"]
    [:br] [:a {:href "/"} " << Back to menu"]
    [:div [:br] [:br] @errorMessage]])


(defn ^:export start []
  (r/render-component [login-app]
                      (.getElementById js/document "root")))