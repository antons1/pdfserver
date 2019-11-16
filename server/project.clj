(defproject pdfserver "0.1.0-SNAPSHOT"
  :description "Plex style document server"
  :dependencies [[aero "1.1.3"] ;; config
                 [org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-json "0.5.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.6.1"]
                 [ring-cors "0.1.13"]
                 [pdf-to-images "0.1.2"]]
  :repl-options {:init-ns pdfserver.core}
  :main pdfserver.core
  :aot [pdfserver.core]
  :profiles {:uberjar {:uberjar-name "pdfserver-standalone.jar"}})
