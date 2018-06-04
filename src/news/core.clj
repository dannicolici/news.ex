(ns news.core
  (:require [persistence.core :as p :refer :all])
  (:gen-class))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))

(defn all-news []
  (sort-by #(:id %)
           (mapcat #(news-for-user (:id %))
                   (p/get-all-users))))

(defn --main [& args]
  (if (= (nth (all-news) 2) (first (news-for-user "seconduser")))
    (println "YES")
    (println "NO")))