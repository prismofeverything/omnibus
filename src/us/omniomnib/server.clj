(ns us.omniomnib.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defn html-page
  [title body & opts]
  (html
   [:html
    [:head
     [:meta {:http-equiv "Content-Type" :content "text/html; charset=utf-8"}]
     [:title title]
     [:link {:rel "stylesheet" :type "text/css" :href (opts :css)}]]
    [:body
     body]]))

(defn omniomnibus-home
  [omni]
)  

(defroutes omniomnibus
  (GET "/" [] "OMNIBUS ANIMAL FLOATING INTELLIGENCE")
  (route/not-found "NONONONNONONONON"))

