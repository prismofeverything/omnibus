(ns com.youdonotexist.server
  (:require [lib.html :as html]
            [polaris.core :as polaris]))

(defn youdonotexist-home
  [request]
  (html/html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["enumerable" "vector" "evader" "youdonotexist"]
    :script "  var you = evader({element: 'youdonotexist', over: statementOver, out: statementOut});"}
   [:a#youdonotexist {:href "#"} "You"]))

(defn youdonotexist-statement
  [request]
  (html/html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["youdonotexist"]}
   [:div#interaction
    [:div#question "What is your best explanation of what you are as a thing in this universe?"]
    [:div#important "(this is important)"]
    [:div#help
     [:a.tiny {:href "/help/"} "(help)"]]
    [:div#statement
     [:form#statement_form {:method "post" :action "/statement/"}
      [:input#statement_input {:type "text" :name "statement"}]
      [:input#accept {:type "image" :src "/img/monolith.png"}]]]]))

(defn youdonotexist-statement-post
  [request]
  (html/html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["youdonotexist"]}
   [:a "YOU ARE A THING"]))

(defn youdonotexist-help
  [request]
  (html/html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["youdonotexist"]}
   [:div#youdonotexist
    [:div "This is possibly the most important thing you have ever done."]
    [:div#help_back
     [:a.tiny {:href "/statement/"} "       (back)"]]]))

(defn youdonotexist-javascript
  [tag]
  (html/html-page
   tag
   {:css ["youdonotexist"]
    :js ["enumerable" "sylvester" "linkage" "math" "canvastext" "flux" tag]
    :script (str "window.onload = " tag ".init;")}
   [:div#shell
    [:canvas {:id tag}]]))

(defn youdonotexist-homeostasis
  [request]
  (youdonotexist-javascript "homeostasis"))

(defn youdonotexist-charge
  [request]
  (youdonotexist-javascript "charge"))

(defn youdonotexist-shapemaker
  [request]
  (youdonotexist-javascript "shapemaker"))

(defn youdonotexist-lost
  [request]
  (html/html-page
   "You Exist Less Than Even Before"
   {:css ["youdonotexist"]
    :js ["enumerable" "vector" "evader" "youdonotexist"]
    :script "  var you = evader({element: 'youdonotexist', over: statementOver, out: statementOut});"}
   [:a#youdonotexist {:href "#"} "NONONONON"]))

(def youdonotexist-routes
  [["/" :home youdonotexist-home]
   ["/statement/" :statement {:GET youdonotexist-statement :POST youdonotexist-statement-post}]
   ["/help/" :help youdonotexist-help]
   ["/homeostasis/" :homeostasis youdonotexist-homeostasis]
   ["/charge/" :charge youdonotexist-charge]
   ["/shapemaker/" :shapemaker youdonotexist-shapemaker]])

(defn youdonotexist
  []
  (-> youdonotexist-routes
      (polaris/build-routes)
      (polaris/router)))
