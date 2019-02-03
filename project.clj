(defproject org.fversnel/unitconversion "0.1.0-SNAPSHOT"
  :description "A Clojure(script) library that allows you to convert from one unit to another"
  :url "https://github.com/fversnel/unit-conversion"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [aysylu/loom "1.0.2"]]
  ; TODO Add clojurescript build
  ; TODO Publish to clojars
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
                 )
