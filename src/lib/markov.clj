(ns lib.markov
  (:use clojure.contrib.str-utils
        clojure.contrib.duck-streams))

(defstruct markov-wheel :slices :total)
(defstruct markov-slice :token :weight)
(defstruct markov-node :token :incoming :outgoing :beginning :ending)
(defstruct markov-chain :nodes :beginning :ending)

(defn empty-wheel [] (struct markov-wheel {} 0))
(defn empty-node [token] (struct markov-node token (empty-wheel) (empty-wheel) (empty-wheel) (empty-wheel)))
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
  (or ((chain :nodes) token) (empty-node token)))

(defn continuing-node
  [chain token terminal value]
  (let [node (or ((chain :nodes) token) (empty-node token))]
    (assoc node terminal (observe-token (node terminal) value))))

(defn add-orientation
  [node orientation token]
  (assoc node orientation (observe-token (node orientation) token)))

(def direction-map {:outgoing :ending :incoming :beginning})

(defn single-link
  [chain from-token to-token direction]
  (assoc-in chain
            [:nodes from-token]
            (add-orientation
             (continuing-node chain from-token (direction-map direction) :false)
             direction to-token)))

(defn add-link
  [chain from-token to-token]
  (single-link (single-link chain from-token to-token :outgoing) to-token from-token :incoming))

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

(defn from-focus
  [chain token direction]
  (if-let [node ((chain :nodes) token)]
    (loop [token token
           path '()
           node node]
      (if (= (spin (node (direction-map direction))) :true)
        path
        (let [follow (spin (node direction))]
          (recur follow (cons follow path) ((chain :nodes) follow)))))))

(defn follow-strand
  [chain]
  (let [beginning (spin (chain :beginning))]
    (cons beginning (reverse (from-focus chain beginning :outgoing)))))

(defn issue-strand
  [chain token]
  (concat (from-focus chain token :incoming) [token] (reverse (from-focus chain token :outgoing))))

(defn read-source
  [path]
  (let [source (re-split #"  " (re-gsub #": " ":" (str-join " " (read-lines path))))]
    (reduce add-token-stream (empty-chain) (map #(re-split #" " %) source))))

(defn generate-statement
  [chain]
  (str-join " " (follow-strand chain)))

(defn generate-response
  [chain focus]
  (str-join " " (issue-strand chain focus)))

