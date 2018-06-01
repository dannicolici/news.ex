(ns persistence.core
  (:require [taoensso.carmine :as car :refer (wcar)]
            [domain.core :refer :all])
  (:gen-class))


(def server-conn {:pool {} :spec {:uri "redis://localhost:6379/"}})
(defmacro wcar* [& body] `(car/wcar server-conn ~@body))

(defn usr-key [id] (str "usr:" id))
(defn news-key [id] (str "nws:" id))
(defn usr-news-key [usr-id] (str "u-n:" usr-id))

(defn init-id-seq []
  (wcar* (car/set "id-seq" 0)))

(defn new-id []
  (let [[_ id]
        (wcar* (car/incr "id-seq")
               (car/get "id-seq"))]
    id))


(defmacro save-by-id* [x key-fn]
  `(wcar* (car/set (~key-fn (:id ~x)) ~x)))
(defmacro get-by-id* [id key-fn]
  `(wcar* (car/get (~key-fn ~id))))

(defn save-user [u] (save-by-id* u usr-key))
(defn get-user-by-id [id] (map->User (get-by-id* id usr-key)))
(defn save-news [n]
  "saves the news object and creates relation to user in a sorted set"
  (wcar*
    (let [u-n-id (usr-news-key (:user-id n))
          news-id-list (wcar* (car/get u-n-id))]
      (car/set (news-key (:id n)) n)
      (car/set u-n-id (into (sorted-set) (conj news-id-list (:id n)))))))
(defn get-news-by-id [id] (map->News (get-by-id* id news-key)))
(defn get-news-by-user-id [user-id]
  (map get-news-by-id (get-by-id* user-id usr-news-key)))

(defn -main [& args]
  ;(println (save-user (user 1 "firstuser" "fname" "lname")))
  ;(println (get-user-by-id 1))
  ;(println (save-news (news 1 1 "some text")))
  ;(println (save-news (news 2 1 "news two")))
  ;(println (get-news-by-id 1))
  ;(println (get-news-by-id 2))
  ;(println (map #(select-keys % [:id :text]) (get-news-by-user-id 1))))
  (init-id-seq)
  (println (str "id: " (new-id))))

