(ns api.core
  (:require [domain.news :as news :refer :all]
            [domain.entities :as d :refer :all]
            [infrastructure.persistence :as p :refer :all]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as r :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes api
           (context "/api" []
             (POST "/user" [id fname lname pwd]
               (if (= 1 (p/save-user! (d/user id fname lname pwd)))
                 (r/status {:body "created"} 201)
                 (r/status {:body "already exists"} 409)))

             (POST "/news/:user-id" [user-id text]
               (if (= 1 (p/save-news! (d/news nil user-id text)))
                 (r/status {:body "created"} 201)
                 (r/status {:body "user doesn't exist"} 409)))

             (GET "/news/:user-id" [user-id]
               (r/response
                 (news/news-for-user user-id)))

             (GET "/news" []
               (r/response
                 (news/all-news)))))

(defroutes api-routes api)

(defroutes non-secure-app (->
                            (wrap-defaults api-routes
                              (assoc-in site-defaults [:security :anti-forgery] false))
                            wrap-json-response))

