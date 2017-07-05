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

