(defproject pdfserver "0.1.0-SNAPSHOT"
  :description "Plex style document server"
  :dependencies [[aero "1.1.3"] ;; config
                 [clj-http "3.10.0"
                  ;; Replaced by jcl-over-slf4j
                  :exclusions [commons-logging]]
                 [org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-json "0.5.0"]
                 [org.clojure/core.cache "0.8.1"]
                 [org.clojure/data.json "0.2.6"]
                 [com.cognitect.aws/dynamodb "726.2.484.0"]
                 [com.cognitect.aws/ssm "742.2.518.0"]
                 [com.cognitect.aws/api "0.8.352"
                  ;; Conflicts with ring-jetty-adapter and core
                  :exclusions [org.eclipse.jetty/jetty-http org.eclipse.jetty/jetty-io]]
                 [com.cognitect.aws/endpoints "1.1.11.631"]
                 [buddy/buddy-auth "2.2.0"
                  :exclusions [org.clojure/clojurescript]]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.slf4j/jcl-over-slf4j "2.0.0-alpha0"] ;; bridge Apache Http Client logs to logback
                 [uk.me.rkd.ttlcache "0.1.0"
                  ;; Use a newer core.cache; still compatible with this:
                  :exclusions [org.clojure/core.cache]]
                 ;; Retrial of requests:
                 [diehard "0.8.4"]
                 [compojure "1.6.1"]
                 [ring-cors "0.1.13"]]
  :repl-options {:init-ns pdfserver.core}
  :main pdfserver.core
  :aot [pdfserver.core]
  :profiles {:uberjar {:uberjar-name "pdfserver-standalone.jar"}})
