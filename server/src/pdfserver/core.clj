(ns pdfserver.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.string :as s]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [pdfserver.pdfserver :as pdfserver])
  (:gen-class))

(defonce server (atom nil))

(defn content-type-set?
  [response]
  (or (:content-type response)
      ((:headers response) "Content-Type")
      ((:headers response) "content-type")))

(defn wrap-default-content-type
  ([handler]
   (wrap-default-content-type handler "application/json"))
  ([handler content-type]
   (fn [request]
     (let [response (handler request)]
       (if (complement (content-type-set? response))
         (assoc-in response [:headers "Content-Type"] content-type)
         response)))))


(defroutes handler
           (GET "/" [] "[\"Hello World\"]")
           (GET "/all" [] (pdfserver/files-in-root))
           (GET "/directory" {{path :path} :params} (pdfserver/folder-content path))
           (route/not-found "Not found"))

(def app
  (-> #'handler
      (wrap-default-content-type)
      (wrap-cors :access-control-allow-origin [#"http://localhost"]
                 :access-control-allow-methods [:get :put :post :delete])
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (wrap-keyword-params)
      (wrap-params)))

(defn start
  ([] (start {:join? false}))
  ([opts]
   (reset! server (jetty/run-jetty #'app (merge {:port (or (System/getenv "PORT") 3000)} opts)))))

(defn stop []
  (when @server
    (.stop ^org.eclipse.jetty.server.Server @server)
    (reset! server nil)))

(defn -main []
  (start {:join? true}))

(comment
  (content-type-set? {:content-type "application/json" :headers {}})
  (start)
  (stop))
