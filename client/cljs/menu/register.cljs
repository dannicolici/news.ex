(ns menu.register
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]))

(def registerSuccess (r/atom false))

(defn register [id fname lname pwd]
  (POST "/api/user" {:format :raw
                     :params {:id id :fname fname :lname lname :pwd pwd}
                     :handler (fn [r] (do
                                        (println r)
                                        (reset! registerSuccess true)))}))
(defn elem [id]
  (.getElementById js/document id))

(defn register-app []
  [:div
   "Username" [:input {:type "text" :name "id" :id "id"}] [:br]
   "First name" [:input {:type "text" :name "fname" :id "fname"}][:br]
   "Last name" [:input {:type "text" :name "lname" :id "lname"}][:br]
   "Password" [:input {:type "password" :name "pwd" :id "pwd"}][:br]
   [:button {:onClick #(register (.-value (elem "id"))
                                 (.-value (elem "fname"))
                                 (.-value (elem "lname"))
                                 (.-value (elem "pwd")))} "Register"]
   (if @registerSuccess
         [:div [:br][:br] "Registered!" [:a {:href "news"} " Go to news"]]
         [:div [:br][:br] "Not registered!"])
   [:br][:a {:href "/"} " << Back to menu"]])


(defn ^:export start []
  (r/render-component [register-app] (elem "root")))