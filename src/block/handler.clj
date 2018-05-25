(ns block.handler
  (:require [news.core :as news :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
           (context "/api" []
             (GET "/news/:user-id" [user-id] (news/feed user-id))))

(defroutes static-routes
           (route/resources "/public"))

(defn- wrap-root [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(if (= "/" %) "/index.html" %)))))


(def app (->
           (routes
             static-routes
             (wrap-defaults app-routes site-defaults)
             (route/not-found "Not Found"))
           wrap-root))
