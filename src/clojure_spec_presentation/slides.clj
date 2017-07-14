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
;;;;;;;;;;;;;; 1. Specs for Validation ;;;;;;;;;;;;;;;;;;;;;
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
;;;;;;;;;;;;;; 2. Specs for Destructuring ;;;;;;;;;;;;;;;;;;
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
;;;;;;;;;;;;;; 3. Specs as Documentation ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; For value or function specs
(spec/describe ::example/special-sequence)
(spec/describe ::example/special-map)


;; Requires fully qualified symbols for functions
(spec/describe `example/multiply-by-three)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; 4. Specs for Error Reporting ;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; A simple example

#_(spec/explain int? "hello")


#_(spec/explain ::example/special-sequence [1 2 "hello" 3])
#_(spec/explain ::example/special-map {::example/team-a {::example/score 100
                                                       ::example/players []}})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; 5. Test data generation ;;;;;;;;;;;;;;;;;;;;;
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
;;;;;;;;;;;;;;  Defining Specs ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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






;; Maps
;; clojure.spec seperates specing the keys for a map and the values
;; of those keys
(spec/def ::value-1 int?)
(spec/def ::value-2 string?)
(spec/def ::opt-value-1 keyword?)
(spec/def ::opt-value-2 symbol?)

;; We can define which keys are required and which keys must be
;; namespace qualified
(spec/def ::map-spec (spec/keys :req    [::value-1]        ;; Namespaced qualified and required
                                :req-un [::value-2]        ;; Required
                                :opt    [::opt-value-1]
                                :opt-un [::opt-value-2]))

(spec/valid? ::map-spec {::value-1 1
                         :value-2 "hello"
                         ::opt-value-1 :hello
                         :opt-value-2 'huh})





;; Collections
(spec/def ::collection-1 (spec/coll-of int?))
(spec/valid? ::collection-1 '(1 2 3 4 5))

;; Compose them with other specs
(spec/def ::collection-2 (spec/coll-of ::food))
(spec/valid? ::collection-2 '("hot dog" "hamburger" "hot dog" "enchilada"))

;; come with lots of options
(spec/def ::collection-3 (spec/coll-of int?
                                       :kind vector?
                                       :min-count 1
                                       :max-count 3
                                       :distinct true
                                       :into '()))
(spec/valid? ::collection-3 [1 2 3])
(spec/conform ::collection-3 [1 2 3])



;; Sequences
;; The api is similar to regular expressions on strings

;; Look at the regular expression #"bye" as
;; a list '(b y e)

;; Use spec/cat in this instance 
;; (Note: using keywords here for ease of use)
(spec/def ::sequence-1 (spec/cat :b #{:b}      ;; spec/cat requires a label for each part
                                 :y #{:y}
                                 :e #{:e}))
(spec/valid? ::sequence-1 '(:b :y :e))


;; #"b.*e" --------> '(b (* any?) e)
;; There is also spec/+ and spec/?
(spec/def ::sequence-2 (spec/cat :b        #{:b}
                                 :anything (spec/* any?)
                                 :e        #{:e}))

(spec/valid? ::sequence-2 '(:b :e))
(spec/conform ::sequence-2 '(:b :e))

(spec/valid? ::sequence-2 '(:b :y :y :e :w :h :e))
(spec/conform ::sequence-2 '(:b :y :y :e :w :h :e))


;; #"b(e|y)e" -----> '(b (alt e y) e)
;; There is also spec/&
(spec/def ::sequence-3 (spec/cat :b      #{:b}
                                 :e-or-y (spec/alt :e #{:e}   ;; spec/alt requires labels
                                                   :y #{:y})
                                 :e      #{:e}))

(spec/valid? ::sequence-3 '(:b :e :e))
(spec/conform ::sequence-3 '(:b :e :e))


;; However, spec can capture much more than keywords.
;; Nest arbitrary specs into sequences

(spec/def ::sequence-4 (spec/cat :start-code        ::integer
                                 :food-orders       (spec/+ ::food)
                                 :0-or-large-number (spec/alt :big-int ::big-integer
                                                              :zero (spec/and int? zero?))
                                 :name              (spec/& string?
                                                            #(not (empty? %)))))

(def example-seq '(1101 "enchilada" "taco" "hamburger" 0 "John"))
(spec/valid? ::sequence-4 example-seq)
(spec/conform ::sequence-4 example-seq)



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


