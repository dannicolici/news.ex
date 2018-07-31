(ns infrastructure.auth)

(defn get-current-username [request]
  (get-in request [:session :cemerick.friend/identity :current]))
