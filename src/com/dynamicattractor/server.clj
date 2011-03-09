(ns com.dynamicattractor.server
  (:use clojure.contrib.str-utils
        compojure.core
        lib.html))

(defn dynamicattractor-home
  []
  (html-page
   "dynamic attractor home"
   {:css ["dynamicattractor"]
    :js ["linkage" "jquery" "dynamicattractor"]
    :script "$(document).ready(function() {dynamicattractor.attach()})"}
   [:div#dynamicattractor
    [:h1.title "Dynamic Attractor"]
    [:h2 ": A home for systems research and interdisciplinary collaboration :"]
    ]))

(defroutes dynamicattractor
  (GET "/" [] (dynamicattractor-home))
  (ANY "*" [] (dynamicattractor-home)))

