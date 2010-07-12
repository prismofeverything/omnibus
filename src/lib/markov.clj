(ns lib.markov
  (:use [clojure.contrib.duck-streams :only (read-lines)]))

(defstruct markov-wheel :slices :total)
(defstruct markov-slice :token :weight)
(defstruct markov-node :token :incoming :outgoing :beginning :ending)
(defstruct markov-chain :nodes :beginnings :endings)

(defn empty-wheel [] (struct markov-wheel {} 0))
(defn empty-chain [] (struct markov-chain {} [] []))

(defn inc-slice [slice]
  (assoc slice :weight (+ (slice :weight) 1)))

(defn wheel-slice [wheel token]
  (or ((wheel :slices) token) (struct markov-slice token 0)))

(defn wheel-weight [wheel token]
  (((wheel :slices) token) :weight))

(defn collect-token [wheel token]
  (let [slice (inc-slice (wheel-slice wheel token))]
    (struct markov-wheel (assoc (wheel :slices) token slice) (+ (wheel :total) 1))))

(defn spin [wheel]
  (let [fate (* (rand) (wheel :total))]
    (loop [tokens (seq (keys (wheel :slices)))
           step 0]
      (let [next-step (+ step (wheel-weight wheel (first tokens)))]
        (if (> fate next-step)
          (recur (next tokens) next-step)
          (first tokens))))))

(defn chain-node [chain token]
  (or ((chain :nodes) token) (struct markov-node token (empty-wheel) (empty-wheel) false false)))

(defn add-node [node orientation token]
  (assoc node orientation (collect-token (node orientation) token)))

(defn add-link [chain from-token to-token]
  (let [[from-node to-node] [(add-node (chain-node chain from-token) :outgoing to-token) (add-node (chain-node chain to-token) :incoming from-token)]]
    (struct markov-chain (assoc (assoc (chain :nodes) from-token from-node) to-token to-node))))
