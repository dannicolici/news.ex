(ns news.core
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]
            [cognitect.transit :as t]))

(defn console [& args]
  (.log js/console (str args)))

(defn error-handler [{:keys [status status-text]}]
  (console "something went wrong: " status " " status-text))

(def as-json (t/reader :json))
(def feed (r/atom nil))
(defn feed-map [] (t/read as-json @feed))

(defn get-all-news []
  (GET "/api/news"
    {:handler       (fn [r] (reset! feed r))
     :error-handler error-handler}))

(defn news-app []
  (get-all-news)
  (fn [] [:div
          [:ul {:id "news"}
           (for [news (feed-map)]
             ^{:key (get news "id")}
             [:li (get news "text") " - " (get news "user-id")])]]))


(defn ^:export start []
  (r/render-component [news-app]
                      (.getElementById js/document "root")))