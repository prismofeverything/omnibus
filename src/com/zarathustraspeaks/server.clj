(ns com.zarathustraspeaks.server
  (:use compojure.core
        ring.util.servlet
        ring.adapter.jetty
        lib.markov
        hiccup.core)
  (:require [compojure.route :as route]))

(def zarathustra-chain (read-source "text/zabbrev.txt"))

(defn zarathustra-home
  [statement]
  (html
   [:html
    [:head
     [:title "Zarathustra SPEAKS"]
     [:link {:rel "stylesheet" :type "text/css" :href "/css/zarathustra.css"}]]
    [:body
     [:div#speaks
      [:p
       [:a#title {:href "/"} "ZARATHUSTRA SAYS:"]]]
     [:div#statement
      [:p statement]]]]))

(defroutes zarathustraspeaks
  (GET "/" [] (zarathustra-home (generate-statement zarathustra-chain)))
  (route/not-found (zarathustra-home (generate-statement zarathustra-chain))))

(def zarathustraspeaks-server (run-jetty zarathustraspeaks {:port 8188 :join? false}))


