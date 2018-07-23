(ns domain.news
  (:require [infrastructure.persistence :as p :refer :all]))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))

(defn all-news []
  (sort-by #(:id %)
           (mapcat #(news-for-user (:id %))
                   (p/get-all-users))))