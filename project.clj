(defproject news "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :cljsbuild {:builds [{:source-paths ["client/cljs"]
                        :compiler     {:optimizations :whitespace
                                       :output-to     "server/priv/static/js/main.js"
                                       :output-dir    "server/priv/static/js"}}]}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]                 
                 [cljs-ajax "0.7.3"]
                 [hiccup "1.0.5"]
                 [org.clojure/core.async "0.4.474"]
                 [reagent "0.8.1"]
                 [clojure.java-time "0.3.2"]
                 [thheller/shadow-client "1.3.2"]
                 [cljsjs/phoenix "1.3.0-0"]
                 [reagent-utils "0.3.2"]]

  :plugins [[lein-cljsbuild "1.1.7"]]
)
