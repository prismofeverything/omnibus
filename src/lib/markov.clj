(ns lib.markov
  (:use clojure.contrib.str-utils
        clojure.contrib.duck-streams))

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
  (or ((wheel :slices) token) (struct markov-slice token 0)))

(defn wheel-weight 
  [wheel token]
  (((wheel :slices) token) :weight))

(defn observe-token 
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
  (or ((chain :nodes) token) (struct markov-node token (empty-wheel) (empty-wheel) (empty-wheel) (empty-wheel))))

(defn continuing-node
  [chain token terminal value]
  (let [node (or ((chain :nodes) token) (struct markov-node token (empty-wheel) (empty-wheel) (empty-wheel) (empty-wheel)))]
    (assoc node terminal (observe-token (node terminal) value))))

(defn add-orientation
  [node orientation token]
  (assoc node orientation (observe-token (node orientation) token)))

(defn add-link
  [chain from-token to-token]
  (let [from (assoc-in chain [:nodes from-token] (add-orientation (continuing-node chain from-token :ending :false) :outgoing to-token))]
    (assoc-in from [:nodes to-token] (add-orientation (continuing-node from to-token :beginning :false) :incoming from-token))))

(defn add-terminal
  [chain terminal token]
  (let [terminated (assoc chain terminal (observe-token (chain terminal) token))]
    (assoc-in terminated [:nodes token] (continuing-node terminated token terminal :true))))

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

(defn follow-strand
  [chain]
  (loop [token (spin (chain :beginning))
         path (list token)
         node ((chain :nodes) token)]
    (if (= (spin (node :ending)) :true)
      (reverse path)
      (let [follow (spin (node :outgoing))]
        (recur follow (cons follow path) ((chain :nodes) follow))))))

(defn read-source
  [path]
  (let [source (re-split #"  " (re-gsub #": " ":" (str-join " " (read-lines path))))]
    (reduce add-token-stream (empty-chain) (map #(re-split #" " %) source))))

(defn generate-statement
  [chain]
  (str-join " " (follow-strand chain)))

