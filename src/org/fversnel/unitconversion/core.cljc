(ns org.fversnel.unitconversion.core
  (:require [org.fversnel.unitconversion.graph :as graph]))

(def ^:private invert-operator
  {'+ '-
   '- '+
   '* '/
   '/ '*})

(defn- invert-formula [formula]
  (cond
    (seq? (first formula))
    (->>
     formula
     vec
     reverse
     (map invert-formula))

    :else
    (let [[operator rate] formula]
      `(~(invert-operator operator) ~rate))))

(defn- formula->fn [formula]
  (cond
    (seq? (first formula))
    (->>
     formula
     vec
     (map formula->fn)
     reverse
     (reduce comp identity))

    :else
    (let [[operator rate] formula]
      (fn [amount]
        (eval `(~operator ~amount ~rate))))))

(defn conversion-graph 
  "Create a graph from a unit map to allow for conversion between units."
  [unit-map]
  (reduce
   (fn [table [from-unit {:keys [conversions] :as props}]]
     (let [unit-props (dissoc props :conversions)
           table (graph/add-nodes table [from-unit unit-props])]
       (reduce
        (fn [table [to-unit conversion-formula]]
          (let [convert-to (formula->fn conversion-formula)
                convert-from (-> conversion-formula
                                 invert-formula
                                 formula->fn)]
            (graph/add-edges
             table
             [from-unit to-unit {:convert convert-to}]
             [to-unit from-unit {:convert convert-from}])))
        table
        (seq conversions))))
   (graph/digraph)
   (seq unit-map)))

(defn convert
  "Convert from one quantity to another.
  
  (convert unit-graph {:amount 5000 :unit :mm} :m)
  => {:amount 5 :unit :m}"
  [unit-graph {:keys [amount unit] :as quantity} to-unit]
  (if (= unit to-unit)
    quantity

    (if-let [path (graph/shortest-path unit-graph unit to-unit)]
      {:amount
      (reduce 
        (fn [amount [_ _ {:keys [convert]}]]
          (convert amount))
        amount
        path)

      :unit to-unit})))