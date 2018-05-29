(defproject block "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler     {:optimizations :whitespace
                                       :output-to     "resources/public/js/main.js"
                                       :output-dir    "resources/public/js"}}]}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [cljs-ajax "0.7.3"]
                 [com.taoensso/carmine "2.18.1"]
                 [reagent "0.8.1"]]

  :plugins [[lein-ring "0.12.4"]
            [lein-cljsbuild "1.1.7"]]

  :ring {:handler block.handler/app}

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
