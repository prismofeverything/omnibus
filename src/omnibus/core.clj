(ns omnibus.core
  (:use ring.util.servlet
        ring.adapter.jetty

        us.omniomnib.server
        com.youdonotexist.server
        com.zarathustraspeaks.server))

(defn server-at
     [routes port]
     (run-jetty routes {:port port :join? false}))

(def server-specs
     [[(var omniomnibus) 5544]
      [(var youdonotexist) 11111]
      [(var zarathustraspeaks) 8188]])

(def servers (map #(apply server-at %) server-specs))

(defn go
  []
  (map #(.start %) servers))


