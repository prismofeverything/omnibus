(defproject omnibus "0.0.1"
  :description "OMNIBUS"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [polaris "0.0.4"]
                 [http-kit "2.1.12"]
                 [ring "1.2.1"]
                 [ring/ring-json "0.2.0"]
                 [caribou/antlers "0.6.1"]
                 [elephantlaboratories "0.0.2"]
                 [hiccup "1.0.5"]]
  :resource-paths ["sites"]
  :main omnibus.core)
