(ns block.handler
  (:require [news.core :as news :refer :all]
            [persistence.core :as p :refer :all]
            [domain.core :as d :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as r :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes app-routes
           (context "/api" []
             (PUT "/user/:id/:fname/:lname/:pwd" [id fname lname pwd]
               (if (= 1 (p/save-user (d/user id fname lname pwd)))
                 (r/status {:body "created"} 201)
                 (r/status {:body "already exists"} 409)))

             (POST "/news/:user-id" [user-id text]
               (if (= 1 (p/save-news (d/news nil user-id text)))
                 (r/status {:body "created"} 201)
                 (r/status {:body "user doesn't exist"} 409)))

             (GET "/news/:user-id" [user-id]
               (r/response
                 (map #(select-keys % [:id :text])
                      (news/news-for-user user-id))))))


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
             (wrap-defaults app-routes
                            (assoc-in site-defaults [:security :anti-forgery] false))
             (route/not-found "Not Found"))
           wrap-root
           wrap-json-response))
