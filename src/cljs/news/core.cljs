(ns news.core
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]
            [cognitect.transit :as t]
            [clojure.core.async :refer [go <! timeout]]))

(defn console [& args]
  (.log js/console (str args)))

(defn error-handler [{:keys [status status-text]}]
  (console "something went wrong: " status " " status-text))

(def as-json (t/reader :json))
(def feed (r/atom nil))
(defn feed-map [] (t/read as-json @feed))
(def current-post (r/atom nil))

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
  [:div
   [:ul {:id "news-reader"}
    (for [news (feed-map)]
      ^{:key (get news "id")}
      [:li (get news "date-time") ": " (get news "text") " - " (get news "user-id")])]])

(defn news-poster []
  [:div
   [:input {:type      "text"
            :value     @current-post
            :on-change #(reset! current-post (-> % .-target .-value))}]
   [:input {:type     "button" :value "Post"
            :on-click #(do
                         (post-news @current-post)
                         (refresh-news))}]])

(defn news-app []
  [:div
   [news-poster]
   [news-reader]])

(defn ^:export start []
  (get-all-news)
  (r/render-component [news-app]
                      (.getElementById js/document "root")))