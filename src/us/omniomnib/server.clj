(ns us.omniomnib.server
  (:use compojure.core
        lib.html))

(defn omniomnibus-home
  []
  (html-page
   "O M N I O M N I B U S"
   {:css ["omniomnibus"]}
   [:div#omniomnibus
    [:div#omnibus "OMNIBUS"]
    [:div#animal "ANIMAL"]
    [:div#floating "FLOATING"]
    [:div#intelligence "INTELLIGENCE"]
    ]))  

(defroutes omniomnibus
  (GET "/" [] (omniomnibus-home))
  (ANY "*" [] "NONONONNONONONON"))

