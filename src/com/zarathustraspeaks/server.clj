(ns com.zarathustraspeaks.server
  (:require [polaris.core :as polaris] 
            [lib.html :as html]
            [lib.markov :as markov]))

(def zarathustra-chain 
  (markov/read-source "text/zarathustra.txt"))

(defn anchorize-token
  [token]
  `[:span [:a {:href ~(format "/%s" token)} ~token] " "])

(defn anchorize-strand
  [strand]
  `[:p ~(map anchorize-token strand)])

(defn zarathustra-page
  [statement]
  (html/html-page
   "Zarathustra SPEAKS"
   {:css ["zarathustra"]}
   [:div#shell
    [:div#speaks
     [:p
      [:a#title {:href "/"} "ZARATHUSTRA SAYS:"]]]
    [:div#statement
     statement]]))

(defn zarathustra-home
  [request]
  (zarathustra-page (anchorize-strand (markov/follow-strand zarathustra-chain))))

(defn zarathustra-key
  [request]
  (zarathustra-page (anchorize-strand (markov/issue-strand zarathustra-chain (-> request :params :spoke)))))

(def zarathustraspeaks-routes
  [["/" :home zarathustra-home]
   ["/:spoke" :spoke zarathustra-key]])

(defn zarathustraspeaks
  []
  (-> zarathustraspeaks-routes
      (polaris/build-routes)
      (polaris/router)))
