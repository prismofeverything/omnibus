(ns com.spasmosis.server
  (:use clojure.contrib.str-utils
        compojure.core
        lib.html))

(defn spasmosis-home
  []
  (html-page
   "spasmosispasmosispasmosispasmosispasmosis"
   {:css ["spasmosis"]
    :js ["linkage" "jquery" "spasmosis"]
    :script "$(document).ready(function() {spasmosis.spasmate()})"}
   [:div#spasmosis]))

(defroutes spasmosis
  (GET "/" [] (spasmosis-home))
  (ANY "*" [] "NONONONNONONONON"))

