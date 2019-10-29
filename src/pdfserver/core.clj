(ns pdfserver.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.instant :refer [read-instant-date]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response content-type]]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:gen-class))

(defonce server (atom nil))

(defn file->map
  [file]
  {:name (.getName file) :path (.getPath file)})

(defn get-files-in-folder
  [folder-path]
  (->> (.listFiles (io/file folder-path))
       (filter #(not (.isDirectory %)))
       (map file->map)))

(defroutes handler
           (GET "/" [] (get-files-in-folder "."))
           (route/not-found "Not found"))

(def app
  (-> #'handler
      (wrap-cors :access-control-allow-origin [#"http://localhost"]
                 :access-control-allow-methods [:get :put :post :delete])
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (wrap-keyword-params)
      (wrap-params)))

(defn start
  ([] (start {:join? false}))
  ([opts]
   (reset! server (jetty/run-jetty #'app (merge {:port 3000} opts)))))

(defn stop []
  (when @server
    (.stop ^org.eclipse.jetty.server.Server @server)
    (reset! server nil)))

(defn -main []
  (start {:join? true}))

(comment

  (start)
  (stop))
