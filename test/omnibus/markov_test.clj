(ns omnibus.markov-test
  (:use [lib.markov] :reload-all)
  (:use [clojure.test]))

(def wheel (collect-token (collect-token (collect-token (collect-token (empty-wheel) "nonon") "beleb") "nonon") "ppppp"))

(deftest markov-test
  (is (= 4 (wheel :total)))
  (is (= 2 ((wheel-slice wheel "nonon") :weight)))
)
