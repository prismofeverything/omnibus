(ns com.zarathustraspeaks.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty
        lib.markov)
  (:require [compojure.route :as route]))

(def zarathustra-chain (read-source "text/zabbrev.txt"))

(defroutes zarathustraspeaks
  (GET "/" [] (generate-statement zarathustra-chain))
  (route/not-found (generate-statement zarathustra-chain)))

(def zarathustraspeaks-server (run-jetty zarathustraspeaks {:port 8188 :join? false}))


