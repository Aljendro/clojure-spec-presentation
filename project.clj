(defproject clojure-spec-presentation "0.1.0-SNAPSHOT"
  :description "clojure.spec walkthrough"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/spec.alpha "0.1.123"]
                 [org.clojure/test.check "0.9.0"]]
  :main ^:skip-aot clojure-spec-presentation.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
