(ns com.zarathustraspeaks.server
  (:use compojure.core
        clojure.contrib.str-utils
        lib.markov
        lib.html))

(def zarathustra-chain (read-source "text/zabbrev.txt"))
;; (def zarathustra-chain (read-source "text/zarathustra.txt"))

(defn anchorize-token
  [token]
  `[:span [:a {:href ~(format "/%s" token)} ~token] " "])

(defn anchorize-strand
  [strand]
  `[:p ~(map anchorize-token strand)])

(defn zarathustra-home
  [statement]
  (html-page
   "Zarathustra SPEAKS"
   {:css ["zarathustra"]}
   [:div#shell
    [:div#speaks
     [:p
      [:a#title {:href "/"} "ZARATHUSTRA SAYS:"]]]
    [:div#statement
     statement]]))

(defn chain-output
  [strand]
  (zarathustra-home (anchorize-strand strand)))

(defroutes zarathustraspeaks
  (GET "/" [] (chain-output (follow-strand zarathustra-chain)))
  (GET "/*" {{spoke "*"} :params} (chain-output (issue-strand zarathustra-chain spoke)))
  (ANY "*" [] "NOT FOUND"))


