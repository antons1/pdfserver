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
            [clojure.string :as str])
  (:gen-class))

(defonce server (atom nil))

(defroutes handler
           (GET "/" [] (str "Hello World!"))
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
