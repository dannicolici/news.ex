(ns block.secure
  (:require [block.api :as api]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [cemerick.friend [workflows :as workflows] [credentials :as creds]]
            [cemerick.friend :as friend]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]))

(def users {"root" {:username "root"
                    :password (creds/hash-bcrypt "admin_password")
                    :roles    #{::admin}}
            "jane" {:username "jane"
                    :password (creds/hash-bcrypt "user_password")
                    :roles    #{::user}}})


(defroutes app-routes
           api/api-routes
           (friend/logout (POST "/logout" [] "Logged out")))

(defroutes public-routes
           (GET "/logout" [] "<form action=\"logout\" method=\"post\"><button>Logout</button></form>")
           (GET "/login" [] "<form action=\"login\" method=\"post\"><input type=\"text\" name=\"username\"/><input type=\"text\" name=\"password\"/><button>Login</button></form>"))


(defroutes static-routes
           (route/resources "/public"))

(defroutes secured-app-routes
           (-> (wrap-routes app-routes
                            friend/wrap-authorize
                            #{::user})
               (friend/authenticate {:credential-fn (partial creds/bcrypt-credential-fn users)
                                     :workflows     [(workflows/interactive-form)]})
               wrap-session
               wrap-params
               wrap-keyword-params))

(defn- wrap-root [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(if (= "/" %) "/index.html" %)))))

(def app (->
           (routes
             static-routes
             public-routes
             (wrap-defaults secured-app-routes
                            (assoc-in site-defaults [:security :anti-forgery] false))
             (route/not-found "Not Found"))
           wrap-root
           wrap-json-response))




