(ns org.fversnel.unitconversion.formula)

(def invert-operator
  {'+ '-
   '- '+
   '* '/
   '/ '*})

(defn complex-formula?
  [formula]
  (seq? (first formula)))

(defn invert-formula [formula]
  (cond
    (complex-formula? formula)
    (->>
     formula
     vec
     reverse
     (map invert-formula))

    :else
    (let [[operator rate] formula]
      `(~(invert-operator operator) ~rate))))

(defn formula->fn [formula]
  (cond
    (complex-formula? formula)
    (->>
     formula
     vec
     (map formula->fn)
     (reduce #(comp %2 %1)))

    :else
    (let [[operator rate] formula
          operator (resolve operator)]
      (fn [amount] (operator amount rate)))))