(ns pdfserver.config
  (:require [aero.core :as aero]))

(defn config
  []
  (aero/read-config (clojure.java.io/resource "config.edn")))

(defn root-folder
  []
  (:root-folder (config)))

(comment
  (root-folder))