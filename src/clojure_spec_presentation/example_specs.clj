(ns clojure-spec-presentation.example-specs
  (:require [clojure.spec.alpha :as spec]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; Specs for slides.clj ;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/def ::special-sequence
  (spec/cat :maybe-integers (spec/* int?)
            :maybe-strings (spec/* string?)
            :maybe-int (spec/? int?)
            :keywords (spec/+ keyword?)))

(spec/def ::score (spec/and int? pos?))
(spec/def ::players (spec/coll-of string?))
(spec/def ::team (spec/keys :req [::score ::players]))

(spec/def ::team-a ::team)
(spec/def ::team-b ::team)
(spec/def ::special-map
  (spec/keys :req [::team-a ::team-b]))


(spec/fdef multiply-by-three
           :args (spec/cat :number int?)
           :ret int?
           :fn #(= (-> % :args :number (* 3))
                   (-> % :ret)))
(defn multiply-by-three
  "Takes an integer and multiplies by three"
  [num]
  (* num 3))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;; Solutions for exercises.clj ;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; 1.

(spec/def ::problem-1 (spec/and int? pos?))

;; 2.

(spec/def ::problem-2 #{:a :b :c})

;; 3.

(spec/def ::problem-3 (spec/and string?
                                #(< 5 (count %))))

;; 4.

(spec/def ::problem-4 (spec/coll-of keyword? :count 3))

;; 5.

(spec/def ::problem-5 (spec/keys :req-un [::problem-1 ::problem-2]))

;; 6.

(spec/def ::problem-6 (spec/cat :first (spec/? string?)
                                :second (spec/coll-of int? :count 3)
                                :third (spec/coll-of keyword? :count 3)))



;; 7.

(spec/def ::problem-7 (spec/cat :presidents #{"Washington" "Harrison"}
                                :tags (spec/alt :strings (spec/coll-of string? :count 3)
                                                :keywords (spec/coll-of keyword? :count 3))))


;; 8.

(spec/def ::problem-8 (spec/cat :orders (spec/alt :keywords (spec/* keyword?)
                                                  :strings (spec/* string?))
                                :numbers (spec/alt :ints (spec/* int?)
                                                   :doubles (spec/* double?))))

;; 9.

(spec/def ::map-collection (spec/coll-of (spec/keys :req-un [::problem-1 ::problem-2]) :count 3))
(spec/def ::mystery-sequence (spec/cat :states #{"California" "Nevada" "New York"}
                                       :continent #{:Europe :Antartica :North-America}
                                       :languages #{"java" "clojure" "C"}))

(spec/def ::problem-9 (spec/keys :req-un [::map-collection ::mystery-sequence]))

