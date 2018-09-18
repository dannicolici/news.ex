(ns news.style
  (:require [shadow.markup.css :refer (defstyled)]))

(def header-cell-properties {:font-size     10
                             :color         "#aaaaaa"
                             :border-bottom "1px solid #ddd"
                             :text-align    "right"})

(def user-cell-properties {:font-size     10
                           :color         "grey"
                           :border-bottom "1px solid #ddd"
                           :text-align    "right"})


; ---------- news reader ----------
(defstyled news-table :table
           [env]
           {:border-collapse "collapse"
            :width           "50%"})

(defstyled news-table-body :tbody
           [env]
           {})

(defstyled news-row :tr
           [env]
           {:vertical-align "bottom"
            "&:hover"       {:background-color "#f0f8ff"}})

(defstyled simple-row :tr
           [env]
           {:vertical-align "bottom"})

(defstyled simple-header-cell :th
           [env]
           header-cell-properties)

(defstyled sorting-header-cell :th
           [env]
           (assoc header-cell-properties
             :cursor "pointer"))

(defstyled news-text-cell :td
           [env]
           {:font-size     21
            :font-family   "cursive"
            :border-bottom "1px solid #ddd"})

(defstyled news-user-cell :td
           [env]
           user-cell-properties)

(defstyled news-timestamp-cell :td
           [env]
           (assoc user-cell-properties
             :width 130))

; ---------- news poster ----------
(defstyled news-form :div
           [env]
           {})

(defstyled news-form-text :textarea
           [env]
           {:display "block"
            :width   "50%"
            :height  100})

(defstyled news-form-submit :input
           [env]
           {:border           "none"
            :font-size        16
            :padding          "14px 40px"
            :background-color "#f0f8ff"
            :box-shadow       "0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19)"})

; ---------- pagination ----------
(defstyled pagination-panel :nav
           [env]
           {})

(defstyled page-link :small
           [env]
           {:cursor   "pointer"
            :padding  "0 10px"
            :color    "#aa5522"
            "&:hover" {:background-color "#f0f8ff"}})

(defstyled page-link-inactive :small
           [env]
           {:padding "0 10px"})

; ---------- common ----------
(defstyled empty-panel :div
           [env]
           {:display "block"
            :margin  "50 0 0 0"})