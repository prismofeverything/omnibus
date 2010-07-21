(ns com.zarathustraspeaks.server
  (:use ring.middleware.params
        compojure.core
        clojure.contrib.str-utils
        lib.markov
        hiccup.core)
  (:require [compojure.route :as route]))

(def zarathustra-chain (read-source "text/zabbrev.txt"))

(defn anchorize-strand
  [strand]
  (map #(html [:a {:href (format "/%s" %1)} %1]) strand))

(defn zarathustra-home
  [statement]
  (html
   [:html
    [:head
     [:meta {:http-equiv "Content-Type" :content "text/html; charset=utf-8"}]
     [:title "Zarathustra SPEAKS"]
     [:link {:rel "stylesheet" :type "text/css" :href "/css/zarathustra.css"}]]
    [:body
     [:div#speaks
      [:p
       [:a#title {:href "/"} "ZARATHUSTRA SAYS:"]]]
     [:div#statement
      [:p statement]]]]))

(defn chain-output
  [strand]
  (zarathustra-home (str-join " " (anchorize-strand strand))))

(defroutes zarathustraspeaks
  (GET "/" [] (chain-output (follow-strand zarathustra-chain)))
  (GET "/*" {{spoke "*"} :params} (chain-output (issue-strand zarathustra-chain spoke)))
  (ANY "*" [] "NOT FOUND"))


