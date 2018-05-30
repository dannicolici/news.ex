(ns news.core
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something went wrong: " status " " status-text)))

(def feed (r/atom nil))

(defn news-app []
  (GET "/api/news/1" {:handler (fn [r] (reset! feed r))})
  [:div @feed])

(defn ^:export start []
  (r/render-component [news-app]
                      (.getElementById js/document "root")))