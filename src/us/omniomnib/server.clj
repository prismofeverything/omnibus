(ns us.omniomnib.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes omniomnibus
  (GET "/" [] "OMNIBUS ANIMAL FLOATING INTELLIGENCE")
  (route/not-found "NONONONNONONONON"))

(def omniomnibus-server (run-jetty omniomnibus {:port 5544 :join? false}))

;; (def omniomnibus-server 
;;      (create-server
;;       {:port 5544 :host "localhost"}
;;       "/*" (servlet omniomnibus)))
