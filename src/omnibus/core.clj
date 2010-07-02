(ns omnibus.core
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes example
  (GET "/" [] "OMNIBUS ANIMAL FLOATING INTELLIGENCE")
  (route/not-found "page not found"))

(run-jetty example {:port 5544})
