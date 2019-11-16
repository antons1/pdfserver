(ns pdfserver.pdfserver
  (:require [clojure.java.io :as io]
            [pdfserver.config :as config]
            [pdf-to-images.core :as pi]))

(declare folder->map)

(defn file->map
  [file]
  {:name(.getName file)
   :path (.getPath file)
   :is-file (not (.isDirectory file))})

(defn handle->map
  ([handle]
   (handle->map handle true))
  ([handle recursive?]
   (if (.isDirectory handle) (folder->map handle recursive?) (file->map handle))))

(defn files-in-folder
  ([folder-path]
   (files-in-folder folder-path true))
  ([folder-path recursive?]
   (->> (.listFiles (io/file folder-path))
        (map #(handle->map % recursive?)))))

(defn folder->map
  ([folder]
   (folder->map folder true))
  ([folder recursive?]
   {:name (.getName folder)
    :path (.getPath folder)
    :is-file false
    :content (if recursive?
               (files-in-folder (.getPath folder))
               (map file->map (.listFiles folder)))}))

(defn files-in-root
  []
  (files-in-folder (config/root-folder)))

(defn folder-content
  [path]
  (files-in-folder (str (config/root-folder) "/" path) false))

(defn thumbnail-from-file!
  "Generate image from one pdf page
  [path] generate image of page 1
  [path page] generate image of page `page`"
  ([path]
   (thumbnail-from-file! path 0))
  ([path page]
   (pi/pdf-to-images (io/file path) pi/image-to-file :start-page page :end-page (+ page 1))))

(comment
  (let [image-paths (pi/pdf-to-images (clojure.java.io/file "./documents/The_Atlantic_-_June_2019.pdf") pi/image-to-file)]
    (prn (str "Images count: " (count image-paths)))
    (map prn image-paths))

  (thumbnail-from-file! "./documents/The_Atlantic_-_June_2019.pdf")

  (System/getProperty "user.dir")
  (map str (.list (io/file ".")))
  (files-in-root)
  (folder-content "./documents"))