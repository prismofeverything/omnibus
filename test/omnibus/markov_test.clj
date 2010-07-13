(ns omnibus.markov-test
  (:use [clojure.contrib.seq-utils :only (includes?)])
  (:use [lib.markov] :reload-all)
  (:use [clojure.test]))

(deftest wheel-test
  (let [wheel (reduce #'wheel-token (empty-wheel) [:nonon :beleb :nonon :ppppp])]
    (is (= 4 (wheel :total)))
    (is (= 2 (-> (wheel-slice wheel :nonon) :weight)))
    (is (= 1 (wheel-weight wheel :beleb)))
    (is (includes? [:nonon :beleb :ppppp] (spin wheel)))
    (is (not (spin (-> (node-for (empty-chain) :nonon) :outgoing))))))

(deftest chain-test
  (let [chain (reduce #(add-link %1 (first %2) (last %2)) (empty-chain) [[:nononon :pelpelp] [:yelyely :bonobon]])]
    (is (= 4 (count (chain :nodes))))
    (is (= 1 (-> (add-terminal chain :beginning :graararaa) :beginning :total)))))

