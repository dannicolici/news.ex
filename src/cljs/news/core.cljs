(ns news.core
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]
            [cognitect.transit :as t]
            [clojure.core.async :refer [go <! timeout]]
            [news.style :refer [news-table
                                news-table-body
                                news-text-cell
                                news-user-cell
                                news-timestamp-cell
                                news-row
                                news-form
                                news-form-text
                                news-form-submit
                                empty-panel]]))

(defn console [& args]
  (.log js/console (str args)))

(defn error-handler [{:keys [status status-text]}]
  (console "something went wrong: " status " " status-text))

(def as-json (t/reader :json))
(def feed (r/atom nil))
(defn feed-map [] (t/read as-json @feed))
(def current-post (r/atom ""))

(defn get-all-news []
  (GET "/api/news"
       {:handler       (fn [r] (reset! feed r))
        :error-handler error-handler}))

(defn refresh-news []
  (go
    (<! (timeout 500))
    (get-all-news)))

(defn post-news [news]
  (POST "/api/news" {:format :raw
                     :params {:text news}}))

(defn news-reader []
  (news-table
    (news-table-body
      (for [news (feed-map)]
        (news-row {:key (get news "id")}
                  (news-text-cell (get news "text"))
                  (news-user-cell (get news "user-id"))
                  (news-timestamp-cell (get news "date-time")))))))

(defn news-poster []
  (news-form
    (news-form-text {:value    @current-post
                     :onChange #(reset! current-post (-> % .-target .-value))})
    (news-form-submit {:type    "button"
                       :value   "Post"
                       :onClick #(do
                                   (post-news @current-post)
                                   (refresh-news))})))

(defn vertical-space []
  (empty-panel ""))

(defn news-app []
  [:div
   [news-poster]
   [vertical-space]
   [news-reader]])

(defn ^:export start []
  (get-all-news)
  (r/render-component [news-app]
                      (.getElementById js/document "root")))