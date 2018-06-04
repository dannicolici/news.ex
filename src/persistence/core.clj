(ns persistence.core
  (:require [taoensso.carmine :as car :refer (wcar)]
            [domain.core :refer :all]
            [password.core :refer :all])
  (:gen-class))


(def server-conn {:pool {} :spec {:uri "redis://localhost:6379/"}})
(def id-seq "id-seq")

(defmacro wcar* [& body] `(car/wcar server-conn ~@body))

(defn flush-db [] (wcar* (car/flushdb)))

(def usr-pref "usr:")
(defn usr-key [id] (str usr-pref id))
(defn news-key [id] (str "nws:" id))
(defn usr-news-key [usr-id] (str "u-n:" usr-id))

(defn init-id-seq []
  (wcar* (car/set id-seq 0)))

(defn new-id []
  (let [[_ id]
        (wcar* (car/incr id-seq)
               (car/get id-seq))]
    id))

(defmacro save-by-id* [x key-fn]
  `(wcar* (car/setnx (~key-fn (:id ~x)) ~x)))

(defmacro get-by-id* [id key-fn]
  `(wcar* (car/get (~key-fn ~id))))

(defn get-entity-by-id [id key-fn ctor]
  (let [entity (get-by-id* id key-fn)]
    (if entity
      (ctor entity)
      nil)))

(defn save-user [u] (save-by-id* u usr-key))

(defn get-user-by-id [id] (get-entity-by-id id usr-key map->User))

(defn get-all-users []
  (map
    #(get-entity-by-id (clojure.string/replace % usr-pref "") usr-key map->User)
    (wcar* (car/keys (str usr-pref "*")))))

(defn save-news [n]
  "saves the news object and creates relation to user in a sorted set
    user needs to exist, otherwise news is not saved"
  (if-not (get-user-by-id (:user-id n))
    0
    (do
      (wcar*
        (let [u-n-id (usr-news-key (:user-id n))
              news-id-list (wcar* (car/get u-n-id))
              news (assoc n :id (new-id))]
          (save-by-id* news news-key)
          (car/set u-n-id (into (sorted-set) (conj news-id-list (:id news))))))
      1)))


(defn get-news-by-id [id] (get-entity-by-id id news-key map->News))

(defn get-news-by-user-id [user-id]
  (map get-news-by-id (get-by-id* user-id usr-news-key)))

(defn -main [& args]
  (flush-db)
  (println (save-user (user "firstuser" "fname" "lname" (md5 "pwd"))))
  (println (save-user (user "seconduser" "xxxxx" "aaa" (md5 "pwd3"))))
  (println (save-news (news nil "firstuser" "some text")))
  (println (save-news (news nil "firstuser" "news two")))
  (println (save-news (news nil "seconduser" "wowowiwa")))
  (println (save-news (news nil "seconduser" "kiki koko")))
  (println (get-all-users)))
  ;(init-id-seq)
  ;(println (str "id: " (new-id))))
  ;(wcar* (car/flushdb)))


