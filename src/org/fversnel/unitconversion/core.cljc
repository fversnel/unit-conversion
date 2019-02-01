(ns org.fversnel.unitconversion.core
  (:require
    [org.fversnel.unitconversion.formula :as f] 
    [org.fversnel.unitconversion.graph :as g]))

(defn conversion-graph 
  "Create a graph from a unit map to allow for conversion between units."
  [unit-map]
  (reduce
   (fn [table [from-unit {:keys [conversions] :as props}]]
     (let [unit-props (dissoc props :conversions)
           table (g/add-nodes table [from-unit unit-props])]
       (reduce
        (fn [table [to-unit conversion-formula]]
          (let [convert-to (f/formula->fn conversion-formula)
                convert-from (-> conversion-formula
                                 f/invert-formula
                                 f/formula->fn)]
            (g/add-edges
             table
             [from-unit to-unit {:convert convert-to}]
             [to-unit from-unit {:convert convert-from}])))
        table
        (seq conversions))))
   (g/digraph)
   (seq unit-map)))

(defn convert
  "Convert from one quantity to another.
  
  (convert unit-graph {:amount 5000 :unit :mm} :m)
  => {:amount 5 :unit :m}"
  [unit-graph {:keys [amount unit] :as quantity} to-unit]
  (if (= unit to-unit)
    quantity

    (if-let [path (g/shortest-path unit-graph unit to-unit)]
      {:amount
      (reduce 
        (fn [amount [_ _ {:keys [convert]}]]
          (convert amount))
        amount
        path)

      :unit to-unit})))