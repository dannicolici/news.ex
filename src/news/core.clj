(ns news.core
  (:require [persistence.core :as p :refer :all]))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))