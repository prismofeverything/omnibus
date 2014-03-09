(ns omnibus.core
  (:use [ring.middleware.resource :only (wrap-resource)]
        [ring.middleware.reload :only (wrap-reload)]
        [ring.middleware.nested-params :only (wrap-nested-params)]
        [ring.middleware.keyword-params :only (wrap-keyword-params)]
        [ring.middleware.json :only (wrap-json-params)]
        [ring.middleware.multipart-params :only (wrap-multipart-params)]
        [ring.middleware.params :only (wrap-params)])
  (:require [org.httpkit.server :as httpkit] 
            [us.omniomnib.server :as omni]
            [com.youdonotexist.server :as you]
            [com.dynamicattractor.server :as dyn]
            [com.spasmosis.server :as spax]
            [com.zarathustraspeaks.server :as zar]
            [elephantlaboratories.server :as eleph]))

(defn server-at
  [app domain port]
  (println (format "starting %s at %s" app port))
  (httpkit/run-server 
   (-> (fn [request] 
         (let [response ((app) request)]
           {:status 200 :body response}))
       (wrap-resource (str domain "/public"))
       (wrap-reload)
       (wrap-json-params)
       (wrap-multipart-params)
       (wrap-keyword-params)
       (wrap-nested-params)
       (wrap-params))
   {:port port}))

(def server-specs
  [[omni/omniomnibus "omniomnib.us" 5544]
   [you/youdonotexist "youdonotexist.com" 11111]
   [spax/spasmosis "spasmosis.com" 8483]
   [dyn/dynamicattractor "dynamicattractor.com" 22111]
   [zar/zarathustraspeaks "zarathustraspeaks.com" 8188]])

(def servers (atom {}))

(defn go
  []
  (let [serve (map #(apply server-at %) server-specs)
        elephant (httpkit/run-server eleph/app {:port 11221})]
    (swap! servers (constantly (cons elephant serve)))))

(defn stop
  []
  (map #(.stop %) servers))

(defn -main
  [& args]
  (doall (go)))
