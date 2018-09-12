(ns domain.news
  (:require [infrastructure.persistence :as p :refer :all]))

(defn news-for-user [user-id]
  (p/get-news-by-user-id user-id))

(defn all-news-sorted-by [criteria]
  (sort-by #(read-string (criteria %))
           (mapcat #(news-for-user (:id %))
                   (p/get-all-users))))

(defn- paginate [news page-size page]
  (let [news-page (nth (partition page-size news) (- page 1) '())
        pages (int (Math/ceil (/ (count news) page-size)))
        result {}]
    (assoc result :news news-page :pages pages)))

(defn all-news
  "This is toy code, but production should
  do filtering/sorting at DB level"
  ([]
   (all-news-sorted-by :id))
  ([sort-criteria page-size page]
   (let [sorted-news (all-news-sorted-by sort-criteria)]
     (paginate sorted-news page-size page))))