(ns cljs.news.core
  (:require [ajax.core :refer [GET POST]]))


(defn handler [response]
      (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
      (.log js/console (str "something bad happened: " status " " status-text)))

(GET "/api/news/danutzulica")