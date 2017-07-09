(ns clojure-spec-presentation.exercises
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as spec.gen]
            [clojure.spec.test.alpha :as spec.test]
            [clojure.test :as test]
            [clojure-spec-presentation.example-specs :as example]))

(spec/fdef test-spec
           :args (spec/cat :spec-kw keyword? :spec any?)
           :ret nil?)
(defn test-spec
  "Tests your spec agains the answer spec, pretty prints failed test cases to *out*"
  [spec-kw spec]
  (let [generated-tests-list (spec.gen/sample
                              (spec/gen
                               (keyword "clojure-spec-presentation.example-specs"
                                        (name spec-kw))))
        failed-tests (filter #(not (spec/valid? spec %)) generated-tests-list)]
    (clojure.pprint/pprint failed-tests)))


;; All problems where generated with spec.gen/sample
;; Each position in the list is one value

;; Example 1
;; => (1.0 2.0 3.2 1.1 101.1 34.1 2.3 5.2 4.5 10.1)
;; Answer: (test-spec :example-1 double?)


;; Problem 1
;; => (2 1 1 28 2 1 2 61 244 78)

(test-spec :problem-1 any?)


;; Problem 2
;; => (:c :a :c :c :b :c :b :a :c :b)

(test-spec :problem-2 any?)


;; Problem 3
;; => ("HRCSW74" "po57gB" "0k7jN4" "GlVUhVV" "sE7vF9" "08Ye4f" "7uJ43T" "Tm99ht" "4N5U74Z" "K2Nd9lI")

(test-spec :problem-3 any?)


;; Problem 4
#_
([:W :* :K]
 [:M/? :Q/I :M*/Dr]
 []
 [:yV1C.+.Nv_!/O4 :s4!- :a.?K0/a5- :Q-5/j?KH :Y25.X-D/J!*x :m.+?-v.O/Toj*
  :!.q.*/L :Nzn.niK.If/I :E++?.K_/I :a.F._L/OL9 :!-2.+!._/_4_ :x7.l+/+
  :UB.z?.W9b!/I :x+.?/_? :m.q*/_k75]
 [:gO34.Ig/f :?P2.nL*81.D/+4B87]
 ...)

(test-spec :problem-4 any?)


;; Problem 5
#_
({:problem-1 1, :problem-2 :b}
 {:problem-1 1, :problem-2 :a}
 {:problem-1 14, :problem-2 :c}
 {:problem-1 3, :problem-2 :c}
 {:problem-1 12, :problem-2 :b}
 {:problem-1 13, :problem-2 :a}
 {:problem-1 84, :problem-2 :a}
 {:problem-1 46, :problem-2 :b}
 {:problem-1 29, :problem-2 :a}
 {:problem-1 2, :problem-2 :b})

(test-spec :problem-5 any?)


;; Problem 6
#_
(("" [0 0 0] [:N :* :+])
 ("" [0 0 0] [:VN :y- :Sn])
 ([0 0 1] [:G*+ :aZK.xLy/* :*_.x/H5n])
 ([0 0 1] [:VA/p :AF!.j7no/!!Y0 :Hn!8])
 ("U" [0 -1 -1] [:z2*K/*B6W :???4._.jl/p!3 :y.o12m._dm.W*9*-/_93])
 ([-6 0 -3] [:**_2+X/+QS :-.*C7Bo4/M :g.Y.+?_0a5.MB/D3l])
 ("3q7"
  [0 0 29]
  [:k*_U?DH.qq_w.U!.Hs7.J!.A4/z_6
   :qZC*_.Q-/iL_zK
   :G?.nq6?4-.iYV.c5FD?t.Ly/J_!69])
 ("y"
  [-6 -17 -5]
  [:X-nx.SVjsu.T.!H-.?62?!/-*
   :DX5.xLx-i+.F.A+/x!kIF8
   :iWcc5k3?.J-e?/T9!x])
 ([-8 -4 0]
  [:Az?E
   :*.*i*-0G_.-K!.m+YEy-/crH9
   :-!?01-+h._9_.-+u.*99_xge.!c15/c9-!-])
 ("a44"
  [0 -1 -1]
  [:I6_6?.QN7s._rL/H9+
   :_E+K.Z+X.Jfz3pc7.T1-wJ.TR-y+Lz*v_.SjS/L9d?-DM5?
   :zU8.+M!!8O.L_lW!hg?.y.JUE*.a4?zH8.V+kzo_r.?rr?7wn/Xrk3qS*]))

(test-spec :problem-6 any?)
