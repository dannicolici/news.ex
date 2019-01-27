(ns news.core
  (:require [ajax.core :refer [POST]]
            [reagent.core :as r]
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
                                empty-panel]]
            [news.websocket :as ws]))

(defn console [& args]
  (.log js/console (str args)))

(def socket (ws/connect! (str
                          "ws://" (-> js/location .-host) "/news")))

(def news-channel (r/atom nil))

(def page-size 4)

(def news-list (r/atom {}))
(def current-post (r/atom ""))
(def current-page (r/atom 1))
(def total-pages (r/atom 0))
(def sort-criteria (r/atom :date-time))

(defn latest-news-handler [r]
  (let [server-json (js->clj r)
        pages (get server-json "pages")
        news (get server-json "news")]
    (reset! news-list news)
    (reset! total-pages pages)))

(defn ws-connect []
  (ws/join-with-handlers! "news:all" socket
                          (fn [ok-resp] (latest-news-handler ok-resp))
                          (fn [err-reason] (console err-reason))))

(defn- send-message [message params]
  (ws/push-with-handlers! @news-channel message (clj->js params)
                       (fn [ok-resp] (latest-news-handler ok-resp))
                       (fn [err-reason] (console err-reason))))

(defn post-news [news] (send-message "create" {:text news}))

(defn sort-news [field]
  (reset! sort-criteria field)
  (reset! current-page 1)
  (send-message "sort" {:sort_by field}))

(defn news-reader []
  (news-table
    (news-table-body
      (simple-row {:key "news-header"}
        (simple-header-cell "")
        (sorting-header-cell {:onClick #(sort-news :user_id)} "written by")
        (sorting-header-cell {:onClick #(sort-news :date_time)} "on date"))
      (for [news @news-list]
        (news-row {:key (get news "id")}
                  (news-text-cell (get news "text"))
                  (news-user-cell (get news "user_id"))
                  (news-timestamp-cell (get news "date_time")))))))

(defn news-poster []
  (news-form
    (news-form-text {:value    @current-post
                     :onChange #(reset! current-post (-> % .-target .-value))})
    (news-form-submit {:type    "button"
                       :value   "Post"
                       :onClick #(do
                                   (post-news @current-post)
                                   (reset! current-page 1))})))
                                  

(defn page-element [page-no]
  (let [p (inc page-no)]
    (if (= p @current-page)
      (page-link-inactive {:key p} p)
      (page-link {:key p
                  :onClick #(do
                              (reset! current-page p)
                              (send-message "get-page" {:page @current-page})
                            )}
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
  (reset! news-channel (ws-connect))
  (r/render-component [news-app]
                      (.getElementById js/document "root")))