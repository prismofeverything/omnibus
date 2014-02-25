(ns com.spasmosis.server
  (:require [lib.html :as html]))

(defn spasmosis-home
  [request]
  (html/html-page
   "spasmosispasmosispasmosispasmosispasmosis"
   {:css ["spasmosis"]
    :js ["linkage" "jquery" "spasmosis"]
    :script "$(document).ready(function() {spasmosis.spasmate()})"}
   [:div#spasmosis]))

(defn spasmosis 
  []
  spasmosis-home)
