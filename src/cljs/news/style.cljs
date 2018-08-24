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
            "&:hover"       {:background-color "#f5f5f5"}})

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
; TODO see how event handlers are supported by shadow
; because these inputs need them
(defstyled news-form :div
           [env]
           {})

(defstyled news-form-text :input
           [env]
           {})

(defstyled news-form-submit :input
           [env]
           {})

; ---------- common ----------
(defstyled empty-panel :div
           [env]
           {:display "block"
            :margin  "50 0 0 0"})