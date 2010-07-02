(ns com.zarathustraspeaks.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes zarathustraspeaks
  (GET "/" [] "OH YES I SEE")
  (route/not-found "NONONONNONONONON"))

(def zarathustraspeaks-server (run-jetty zarathustraspeaks {:port 8188 :join? false}))
