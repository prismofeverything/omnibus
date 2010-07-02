(ns omnibus.core
  (:require [us.omniomnib server]
            [com.zarathustraspeaks server]))

(defn go []
  (.start us.omniomnib.server/omniomnibus-server)
  (.start com.zarathustraspeaks.server/zarathustraspeaks-server))
