(ns org.fversnel.unitconversion.spec
  (:require [clojure.spec.alpha :as s]
            [org.fversnel.unitconversion.formula :as formula]))

(s/def ::rate
  (s/or :int (s/and int? #(not= 0 %))
        :ratio ratio?))

(s/def ::basic-formula
  (s/cat :operator #{'+ '- '* '/} :rate ::rate))

(s/def ::complex-formula (s/+ ::formula))

(s/def ::formula
  (s/or
   :formula ::basic-formula
   :complex-formula ::complex-formula))

(s/fdef formula/invert-operator
  :args (s/cat :operator ::operator)
  :ret ::operator
  :fn not=)

(s/fdef formula/complex-formula?
  :args (s/cat :formula ::formula)
  :ret boolean?)

(s/fdef formula/invert-formula
  :args (s/cat :formula ::formula)
  :ret ::formula)

(s/fdef formula/formula->fn
  :args (s/cat :formula ::formula)
  :ret fn?)

; Scratchpad

(comment
  (defn testconvert [x]
    (let [to (f/formula->fn example-formula)
          from (f/formula->fn (f/invert-formula example-formula))
          converted-value (to x)
          original-value (from converted-value)]
      (println "converted value" converted-value \newline "original value" original-value)))
      )