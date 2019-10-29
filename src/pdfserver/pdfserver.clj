(ns pdfserver.pdfserver
  (:require [clojure.java.io :as io]
            [pdfserver.config :as config]))

(defn file->map
  [file]
  {:name(.getName file)
   :path (.getPath file)
   :is-file (not (.isDirectory file))})

(defn folder->map
  ([folder]
   (folder->map folder true))
  ([folder recursive?]
   {:name (.getName folder)
    :path (.getPath folder)
    :is-file false
    :content (if recursive?
               (get-files-in-folder (.getPath folder))
               (map file->map (.listFiles folder)))}))

(defn handle->map
  ([handle]
   (handle->map handle true))
  ([handle recursive?]
   (if (.isDirectory handle) (folder->map handle recursive?) (file->map handle))))

(defn get-files-in-folder
  ([folder-path]
   (get-files-in-folder folder-path true))
  ([folder-path recursive?]
   (->> (.listFiles (io/file folder-path))
        (map #(handle->map % recursive?)))))

(defn get-files-in-root
  []
  (get-files-in-folder (config/root-folder)))

(defn list-folder
  [path]
  (get-files-in-folder (str (config/root-folder) "/" path) false))

(comment
  (System/getProperty "user.dir")
  (map str (.list (io/file ".")))
  (get-files-in-root)
  (list-folder "./documents"))