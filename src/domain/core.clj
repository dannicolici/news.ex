(ns domain.core)

(defrecord User
  [^Integer id
   ^String username
   ^String first-name
   ^String last-name])
(defn user [id username first-name last-name]
  (->User id username first-name last-name))

(defrecord News
  [^Integer id
   ^Integer user-id
   ^String text])
(defn news [id user-id text]
  (->News id user-id text))
