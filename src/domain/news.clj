(ns domain.news
  (:require [persistence.core :as p :refer :all])
  (:gen-class))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))

(defn all-news []
  (sort-by #(:id %)
           (mapcat #(news-for-user (:id %))
                   (p/get-all-users))))