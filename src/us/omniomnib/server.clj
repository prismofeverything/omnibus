(ns us.omniomnib.server
  (:use clojure.contrib.str-utils
        compojure.core
        lib.html))

(def universe {})

(defn omniomnibus-home
  []
  (html-page
   "O M N I O M N I B U S"
   {:css ["omniomnibus"]
    :js ["linkage" "jquery" "omniomnibus"]
    :script "$(document).ready(function() {omniomnibus.attach()})"}
   [:div#omniomnibus
    [:div#omnibus.floating
     [:a {:href "/omnibus"} "OMNIBUS"]]
    [:div#animal.floating "ANIMAL"]
    [:div#floating.floating "FLOATING"]
    [:div#intelligence.floating "INTELLIGENCE"]
    ]))

(defn parse-key
  [key]
  (re-split #"/" key))

(defn omniomnibus-defined
  [key body]
  (html-page
   (format "O M N I O M N I B U S - %s" key)
   {:css ["omniomnibus"]
    :js ["linkage" "jquery" "omniomnibus"]}
   [:div#omniomnibus body]))

(defn omniomnibus-undefined
  [key]
  (html-page
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
  [key]
  (let [pad (get-in universe (parse-key key))]
    (if (empty? pad)
      (omniomnibus-undefined key)
      (omniomnibus-defined key pad))))

(defn omniomnibus-define
  [key body]
  (prn "%s %s" key body)
  (def universe (assoc-in universe (parse-key key) body))
  (omniomnibus-defined key body))

(defroutes omniomnibus
  (GET "/" [] (omniomnibus-home))
  (GET "/*" {{key "*"} :params} (omniomnibus-key key))
  (POST "/*" {{key "*" body "body"} :params} (omniomnibus-define key body))
  (ANY "*" [] "NONONONNONONONON"))

