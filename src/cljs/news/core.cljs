(ns news.core
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]
            [cognitect.transit :as t]
            [clojure.core.async :refer [go <! timeout]]
            [news.style :refer [news-table
                                news-table-body
                                simple-header-cell
                                sorting-header-cell
                                news-text-cell
                                news-user-cell
                                news-timestamp-cell
                                news-row
                                simple-row
                                news-form
                                news-form-text
                                news-form-submit
                                pagination-panel
                                page-link
                                page-link-inactive
                                empty-panel]]))

(defn console [& args]
  (.log js/console (str args)))

(defn error-handler [{:keys [status status-text]}]
  (console "something went wrong: " status " " status-text))

(def as-json (t/reader :json))
(def page-size 4)

(def news-list (r/atom {}))
(def current-post (r/atom ""))
(def current-page (r/atom 1))
(def total-pages (r/atom 0))
(def sort-criteria (r/atom :date-time))

(defn response-handler [r]
  (let [server-json (t/read as-json r)
        pages (get server-json "pages")
        news (get server-json "news")]
    (reset! news-list news)
    (reset! total-pages pages)))

(defn get-paginated-news []
  (GET "/api/news"
       {:handler       response-handler
        :error-handler error-handler
        :params {:sort-by @sort-criteria
                 :page-size page-size
                 :page @current-page}}))

(defn refresh-news []
  (go
    (<! (timeout 500))
    (get-paginated-news)))

(defn post-news [news]
  (POST "/api/news" {:format :raw
                     :params {:text news}}))

(defn sort-news [field]
  (reset! sort-criteria field)
  (reset! current-page 1)
  (get-paginated-news))

(defn news-reader []
  (news-table
    (news-table-body
      (simple-row {:key "news-header"}
        (simple-header-cell "")
        (sorting-header-cell {:onClick #(sort-news :user-id)} "written by")
        (sorting-header-cell {:onClick #(sort-news :date-time)} "on date"))
      (for [news @news-list]
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
                                   (reset! current-page 1)
                                   (refresh-news))})))

(defn page-element [page-no]
  (let [p (inc page-no)]
    (if (= p @current-page)
      (page-link-inactive p)
      (page-link {:onClick #(do
                              (reset! current-page p)
                              (get-paginated-news))}
                 p))))

(defn page-links []
  (pagination-panel "Pages:"
    (map page-element (range @total-pages))))


(defn vertical-space []
  (empty-panel ""))

(defn news-app []
  [:div
   [news-poster]
   [vertical-space]
   [news-reader]
   [page-links]
   [vertical-space]
   [:form {:action "logout" :method "post"} [:button "Logout"]]])


(defn ^:export start []
  (get-paginated-news)
  (r/render-component [news-app]
                      (.getElementById js/document "root")))