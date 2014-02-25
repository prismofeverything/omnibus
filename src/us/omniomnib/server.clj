(ns us.omniomnib.server
  (:require [clojure.string :as string] 
            [polaris.core :as polaris]
            [lib.html :as html]))

(def universe (atom {}))

(defn omniomnibus-home
  [request]
  (html/html-page
   "O M N I O M N I B U S"
   {:css ["omniomnibus"]
    :js ["linkage" "jquery" "omniomnibus"]
    :script "$(document).ready(function() {omniomnibus.attach()})"}
   [:div#omniomnibus
    [:div#omnibus.floating
     [:a {:href "/omnibus"} "OMNIBUS"]]
    [:div#animal.floating "ANIMAL"]
    [:div#floating.floating "FLOATING"]
    [:div#intelligence.floating "INTELLIGENCE"]]))

(defn parse-key
  [key]
  (string/split key #"/"))

(defn omniomnibus-ik
  [request]
  (html/html-page
   "O M N I O M N I B U S - ik"
   {:css ["omniomnibus"]
    :js ["ik"]
    :script "window.onload = ik.ik;"}
   [:div#omniomnibus
    "<canvas id=\"ik\"></canvas>"]))

(defn omniomnibus-defined
  [key body]
  (html/html-page
   (format "O M N I O M N I B U S - %s" key)
   {:css ["omniomnibus"]
    :js ["linkage" "jquery" "omniomnibus"]}
   [:div#omniomnibus body]))

(defn omniomnibus-undefined
  [key]
  (html/html-page
   (format "O M N I O M N I B U S - %s" key)
   {:css ["omniomnibus"]
    :js ["linkage" "jquery" "omniomnibus"]
    :script "$(document).ready(function() {omniomnibus.undefine()})"}
   [:div#undefined
    [:div#notifier
     (format "%s  " key)
     [:a#undefined-link {:href "#define"} "U N D E F I N E D"]]
    [:div#define 
     [:form#definition {:method "POST" :action (format "/%s" key)}
      [:div [:textarea {:name "body" :rows 11 :columns 311}]]
      [:div#enter [:a.submit {:href "javascript:omniomnibus.submitDefinition()"} "enter"]]]]]))
  
(defn omniomnibus-key
  [request]
  (let [key (-> request :params :key) 
        pad (get-in @universe (parse-key key))]
    (if (empty? pad)
      (omniomnibus-undefined key)
      (omniomnibus-defined key pad))))

(defn omniomnibus-define
  [request]
  (let [key (-> request :params :key)
        body (-> request :params :body)]
    (println request)
    (println "creating %s %s" key body)
    (swap! universe assoc-in (parse-key key) body)
    (omniomnibus-defined key body)))

(def omniomnibus-routes
  [["/" :home omniomnibus-home]
   ["/ik" :ik omniomnibus-ik]
   ["/:key" :key {:GET omniomnibus-key 
                  :POST omniomnibus-define}]])

(defn omniomnibus
  []
  (-> omniomnibus-routes
      (polaris/build-routes)
      (polaris/router)))
