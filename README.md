# Unit converters

A Clojure(script) library that allows you to convert from one unit to another.

## Usage

You can use the library by loading the unit map into memory first.

```clojure
(ns example.core
  (:require [clojure.edn :as edn]
            [org.fversnel.unitconversion.core :as unitconv])

; Load the unit map into memory
(def units (edn/read-string (slurp "units.edn")))

; The conversion graph will allow the system to
; convert between arbitrary units as long as there's
; a conversion path between them.
(def convert 
  (partial 
    unitconv/convert 
    (unitconv/conversion-graph units)))
```

You can then use the `convert` function to convert between different units,
like so:

```clojure
(convert {:amount 5000 :unit :mm} :m)
=> {:amount 5 :unit :m}
```

## Creating a unit map

To add units to the unit map you only need to provide an
entry for that unit in the map and a conversion from or to it.

Let's take the following unit map as an example:

```clojure
{:mm {:system :metric
      :notation "mm"
      :conversions {:cm '(/ 10)}}
 :cm {:system :metric
      :notation "cm"
      :conversions {:m '(/ 100)}}
 :m {:system :metric
     :notation "m"}}
```

The library is smart enough that if you provide a conversion from
`cm` → `m` it will automatically add a conversion for `m` → `cm`.

The example is enough for the system to convert from any unit to any other unit. 
For example converting from to `mm` → `m` will work
because it will know how to convert from `mm` → `cm` and then to `m`.

Each attribute will automatically be stored as a node attribute 
in the conversion graph.
The `system` and `notation` attributes are optional, you can also define 
and add attributes yourself.

## Dependencies

Unit conversion's only dependency is Loom, which it uses to create
a conversion graph for the units.

## License

Copyright © 2019 Frank Versnel

Distributed under the Eclipse Public License version 1.0.
