(ns clojure-spec-presentation.slides
  (:require [clojure-spec-presentation.example-specs :as example]
            [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as spec.gen]
            [clojure.spec.test.alpha :as spec.test]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Prerequisites ;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Clojure contains a lot of different predicate functions.
;; They return false or true

(int? 1000)
(int? :hello)
(nil? nil)
(nil? "hello")
(zero? 0)
(pos? 29)
(string? "hello")
(instance? Integer (Integer. 1))
(instance? Double (Double. 1.0))


;; Make your own predicate functions
(defn greater-than-1000?
  "Returns true if the val is greater than 1000, false otherwise"
  [val]
  (> val 1000))

(greater-than-1000? 1001)


;; A shortcut for the above function
(#(> % 1000) 1001)


;; Sets can be used as tests for membership
;; Return truthy/falsy values
(#{"hello" "goodbye"} "what")
(#{"hello" "goodbye"} "hello")



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Specs for Validation ;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; The simplest use of validation is to use a bare predicate
(spec/valid? int? 1000)
;; Or even a set
(spec/valid? #{"John" "Carl"} "Carl")

;; Now lets use registered specs I have defined in the 'example-specs ns

;; spec/valid? uses a spec to validate some arbitrary data
;; similar to the truthy/falsy functions above.
(spec/valid? ::example/special-sequence [1 2 3 "hello" "goodbye" 3 :what])
(spec/valid? ::example/special-sequence [:nothing])
(spec/valid? ::example/special-sequence [1 2 "hello" 3])

;; we can even validate maps and their keys
(spec/valid? ::example/special-map {::example/team-a {::example/score 100
                                                      ::example/players ["John" "Carl"]}
                                    ::example/team-b {::example/score 190
                                                      ::example/players ["Dave" "Bird"]}})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Specs for Destructuring ;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; spec comes with a handy tool to parse arbitrary data
(spec/conform ::example/special-sequence [1 2 3 "hello" "goodbye" 3 :what])
(spec/conform ::example/special-sequence [:nothing])
;; Shouldn't conform to the spec...
(spec/conform ::example/special-sequence [1 2 "hello" 3])


(spec/conform ::example/special-map {::example/team-a {::example/score 100
                                                       ::example/players ["John" "Carl"]}
                                     ::example/team-b {::example/score 190
                                                       ::example/players ["Dave" "Bird"]}})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Specs as Documentation ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; For value or function specs
(spec/describe ::example/special-sequence)
(spec/describe ::example/special-map)


;; Requires fully qualified symbols for functions
(spec/describe `example/multiply-by-three)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Specs for Error Reporting ;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; A simple example
(spec/explain int? "hello")


(spec/explain ::example/special-sequence [1 2 "hello" 3])
(spec/explain ::example/special-map {::example/team-a {::example/score 100
                                                       ::example/players []}})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Test data generation ;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Generating just one value from a given spec

;; Some simple examples
(spec.gen/generate (spec/gen int?))
(spec.gen/generate (spec/gen #{:one :two :three}))

;; These can get pretty big, try them anyways!
(spec.gen/generate (spec/gen ::example/special-sequence))
(spec.gen/generate (spec/gen ::example/special-map))


;; Generating a sample of a given spec, default 10 values

;; Some simple examples
(spec.gen/sample (spec/gen string?))
(spec.gen/sample (spec/gen (spec/and int? #(> % 1000))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Generative test generation ;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Defining Specs ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Specs must always be fully qualified keywords
;; Here we have a simple spec using a clojure builtin predicate

(spec/def ::integer int?)
(spec/valid? ::integer 1000)



;; We can supply our own predicates and combine them with
;; spec/and and spec/or
(spec/def ::big-integer (spec/and int?
                                  #(> % 1000)))
(spec/valid? ::big-integer 1001)

;; spec/or must supply keywords so that when we parse the data
;; we know which path it took.
(spec/def ::food (spec/or :american #{"hot dog" "hamburger"}
                          :mexican #{"enchilada" "taco"}))
(spec/valid? ::food "hamburger")
(spec/conform ::food "hamburger")



;; Spec'ing functions
;; :fn takes a function that accepts a map, which contains
;; args and ret value. {:args ... :ret ...}
 
(spec/fdef return-num
           :args (spec/cat :num ::integer)
           :ret ::integer
           ;; Access the :args and the named value :num
           ;; Also access the :ret value
           :fn #(= (-> % :args :num)
                   (-> % :ret)))
(defn return-num
  "Returns the number passed into the function"
  [number]
  number)



;; Further API examples continued at bottom for later viewing


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; x. Defining Specs (Cont'd) ;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

