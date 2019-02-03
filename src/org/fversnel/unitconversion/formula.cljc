(ns org.fversnel.unitconversion.formula)

(def invert-operator
  {'+ '-
   '- '+
   '* '/
   '/ '*})

(def resolve-operator
  {'+ +
   '- -
   '* *
   '/ /})

(defn complex-formula?
  [[formula & _]]
  (seq? formula))

(defn invert-formula [formula]
  (cond
    (complex-formula? formula)
    (into '() (map invert-formula) formula)

    :else
    (let [[operator rate] formula]
      (list (invert-operator operator) rate))))

(defn formula->fn [formula]
  (cond
    (complex-formula? formula)
    (transduce
     (map formula->fn)
     (completing #(comp %2 %1))
     identity
     formula)

    :else
    (let [[operator rate] formula
          operator (resolve-operator operator)]
      (fn [amount] (operator amount rate)))))