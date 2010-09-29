(ns com.youdonotexist.server
  (:use compojure.core
        ring.adapter.jetty
        lib.html)
  (:require [compojure.route :as route]))

(defn youdonotexist-home
  []
  (html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["enumerable" "vector" "evader" "youdonotexist"]
    :script "  var you = evader({element: 'youdonotexist', over: statementOver, out: statementOut});"}
   [:a#youdonotexist {:href "#"} "You"]))

(defn youdonotexist-statement
  []
  (html-page
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
      [:input#accept {:type "image" :src "/img/monolith.png"}]]]]
   ))

(defn youdonotexist-statement-post
  []
  (html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["youdonotexist"]}
   [:a "YOU ARE A THING"]))

(defn youdonotexist-help
  []
  (html-page
   "You Do Not Exist"
   {:css ["youdonotexist"]
    :js ["youdonotexist"]}
   [:div#youdonotexist
    [:div "This is possibly the most important thing you have ever done."]
    [:div#help_back
     [:a.tiny {:href "/statement/"} "       (back)"]]]))

(defn youdonotexist-javascript
  [tag]
  (html-page
   tag
   {:css ["youdonotexist"]
    :js ["enumerable" "sylvester" "linkage" "math" "canvastext" "flux" tag]
    :script (str "window.onload = " tag ".init;")}
   [:div#shell
    [:canvas {:id tag}]]))

(defn youdonotexist-homeostasis
  []
  (youdonotexist-javascript "homeostasis"))

(defn youdonotexist-charge
  []
  (youdonotexist-javascript "charge"))

(defn youdonotexist-shapemaker
  []
  (youdonotexist-javascript "shapemaker"))

(defn youdonotexist-lost
  []
  (html-page
   "You Exist Less Than Even Before"
   {:css ["youdonotexist"]
    :js ["enumerable" "vector" "evader" "youdonotexist"]
    :script "  var you = evader({element: 'youdonotexist', over: statementOver, out: statementOut});"}
   [:a#youdonotexist {:href "#"} "NONONONON"]))

(defroutes youdonotexist
  (GET "/" [] (youdonotexist-home))
  (GET "/statement/" [] (youdonotexist-statement))
  (POST "/statement/" [] (youdonotexist-statement-post))
  (GET "/help/" [] (youdonotexist-help))
  (GET "/homeostasis/" [] (youdonotexist-homeostasis))
  (GET "/charge/" [] (youdonotexist-charge))
  (GET "/shapemaker/" [] (youdonotexist-shapemaker))
  (ANY "*" [] (youdonotexist-lost)))

