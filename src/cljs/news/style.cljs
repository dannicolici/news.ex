(ns news.style
  (:require [shadow.markup.css :refer (defstyled)]))


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
           {:font-size     10
            :color         "#aaaaaa"
            :border-bottom "1px solid #ddd"
            :text-align    "right"})

(defstyled sorting-header-cell :th
           [env]
           {:font-size     10
            :color         "#aaaaaa"
            :border-bottom "1px solid #ddd"
            :text-align    "right"
            :cursor        "pointer"})

(defstyled news-text-cell :td
           [env]
           {:font-size     21
            :font-family   "cursive"
            :border-bottom "1px solid #ddd"})

(defstyled news-user-cell :td
           [env]
           {:font-size     10
            :color         "grey"
            :border-bottom "1px solid #ddd"
            :text-align    "right"})

(defstyled news-timestamp-cell :td
           [env]
           {:font-size     10
            :color         "grey"
            :border-bottom "1px solid #ddd"
            :text-align    "right"
            :width         130})

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

; ---------- common ----------
(defstyled empty-panel :div
           [env]
           {:display "block"
            :margin  "50 0 0 0"})