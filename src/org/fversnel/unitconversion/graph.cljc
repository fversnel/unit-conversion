(ns org.fversnel.unitconversion.graph
    (:require [loom.graph :as graph]
              [loom.attr :as attr]
              [loom.alg :as alg]))
  
  (def digraph graph/digraph)
  
  (defn- add-attrs [add-attr-fn g x attrs]
    (reduce
     (fn [g [key value]]
       (add-attr-fn g key value [x]))
     g
     (seq attrs)))
  
  (def add-node-attrs (partial add-attrs attr/add-attr-to-nodes))
  
  (def add-edge-attrs (partial add-attrs attr/add-attr-to-edges))
  
  (defn add-nodes [g & nodes]
    (reduce
     (fn [g [node attrs]]
       (-> g
           (graph/add-nodes node)
           (add-node-attrs node attrs)))
     g
     nodes))
  
  (defn add-edges [g & edges]
    (reduce
     (fn [g [from to attrs]]
       (-> g
           (graph/add-edges [from to])
           (add-edge-attrs [from to] attrs)))
     g
     edges))
  
  (defn shortest-path [g src dest]
    (if-let [path (alg/shortest-path g src dest)]
      (->>
       path
       (partition 2 1)
       (map
        (fn [[from to]]
          [from to (attr/attrs g from to)])))))