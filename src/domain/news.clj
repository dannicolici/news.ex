(ns domain.news
  (:require [infrastructure.persistence :as p :refer :all]))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))

(defn- all-news-sorted-by [field]
  (sort-by field
           (mapcat #(news-for-user (:id %))
                   (p/get-all-users))))

(defn all-news [] (all-news-sorted-by :id))

(defn paginated-news [sort-criteria page-size page]
  "This is toy code, but production should
  do filtering/sorting at DB level"
  (let [sorted-news (all-news-sorted-by sort-criteria)
        news-page (nth (partition page-size page-size nil sorted-news) (- page 1) '())
        pages (int (Math/ceil (/ (count sorted-news) page-size)))
        result {}]
    (assoc result :news news-page :pages pages)))