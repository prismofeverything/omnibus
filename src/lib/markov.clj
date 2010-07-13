(ns lib.markov
  (:use [clojure.contrib.duck-streams :only (read-lines)]))

(defstruct markov-wheel :slices :total)
(defstruct markov-slice :token :weight)
(defstruct markov-node :token :incoming :outgoing :beginning :ending)
(defstruct markov-chain :nodes :beginning :ending)

(defn empty-wheel [] (struct markov-wheel {} 0))
(defn empty-chain [] (struct markov-chain {} (empty-wheel) (empty-wheel)))

(defn inc-slice 
  [slice]
  (assoc slice :weight (+ (slice :weight) 1)))

(defn wheel-slice 
  [wheel token]
  (or (-> wheel :slices token) (struct markov-slice token 0)))

(defn wheel-weight 
  [wheel token]
  ((-> wheel :slices token) :weight))

(defn wheel-token 
  [wheel token]
  (let [slice (inc-slice (wheel-slice wheel token))]
    (struct markov-wheel (assoc (wheel :slices) token slice) (+ (wheel :total) 1))))

(defn spin 
  [wheel]
  (if (empty? (wheel :slices))
    nil
    (let [fate (* (rand) (wheel :total))]
      (loop [tokens (seq (keys (wheel :slices)))
             step 0]
        (let [next-step (+ step (wheel-weight wheel (first tokens)))]
          (if (> fate next-step)
            (recur (next tokens) next-step)
            (first tokens)))))))

(defn node-for
  [chain token]
  (or (-> chain :nodes token) (struct markov-node token (empty-wheel) (empty-wheel) false false)))

(defn add-token 
  [node orientation token]
  (assoc node orientation (wheel-token (node orientation) token)))

(defn add-link 
  [chain from-token to-token]
  (let [[from-node to-node] [(add-token (node-for chain from-token) :outgoing to-token) (add-token (node-for chain to-token) :incoming from-token)]]
    (assoc chain :nodes (assoc (assoc (chain :nodes) from-token from-node) to-token to-node))))

(defn add-terminal
  [chain terminal token]
  (assoc chain terminal (wheel-token (chain terminal) token)))

(defn add-token-stream
  [chain tokens]
  (let [head (first tokens)]
    (if head
      (loop [chain (add-terminal chain :beginning head)
             tokens (rest tokens)
             previous head]
        (if (empty? tokens)
          (add-terminal chain :ending previous)
          (recur (add-link chain previous (first tokens)) (rest tokens) (first tokens)))))))

