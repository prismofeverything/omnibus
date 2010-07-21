(ns us.omniomnib.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes omniomnibus
  (GET "/" [] "OMNIBUS ANIMAL FLOATING INTELLIGENCE")
  (route/not-found "NONONONNONONONON"))

