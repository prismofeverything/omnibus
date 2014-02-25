(ns com.dynamicattractor.server
  (:require [polaris.core :as polaris]
            [lib.html :as html]))

(defn dynamicattractor-home
  [request]
  (html/html-page
   "dynamic attractor home"
   {:css ["dynamicattractor"]
    :js ["linkage" "jquery" "dynamicattractor"]
    :script "$(document).ready(function() {dynamicattractor.attach()})"}
   [:div#dynamicattractor
    [:h1.title "! Dynamic Attractor !"]
    [:h2 ": A home for systems research and interdisciplinary collaboration :"]
    ]))

(def dynamicattractor-routes
  [["/" :home dynamicattractor-home]])

(defn dynamicattractor
  []
  (-> dynamicattractor-routes
      polaris/build-routes
      polaris/router))
