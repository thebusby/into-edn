(ns into-edn-test
  (:use clojure.test
        into-edn))

(def simple-tests [{:title "Just an fn"
                    :spec count
                    :target "12345"
                    :result 5}

                   {:title "Just a vec"
                    :spec [identity inc]
                    :target (range 5)
                    :result [1 2 3 4 5]}

                   {:title "Just a map"
                    :spec {:inc inc
                           :dec dec}
                    :target 3
                    :result {:inc 4, :dec 2}}

                   {:title "Map with literals, into, and merge"
                    :spec {:copied :literal
                           :another-literal 1
                           :into-edn/into #(get % :nested)
                           :into-edn/merge {:a 1 :c 3 :e 5}}
                    :target {:skipped :value :a :overwritten-value :nested {:b 2 :c 4 :d 4}}
                    :result {:copied :literal :another-literal 1 :a 1 :b 2 :c 3 :d 4 :e 5}}

                   {:title "More nested specs"
                    :spec {:ident identity
                           :inc inc
                           :dec dec
                           :range range
                           :range-vec [range]
                           :range-inc [range
                                       inc]
                           :range-map [range
                                       {:inc inc
                                        :dec dec}]
                           :nil-vec [(fn [_] nil)
                                     inc]
                           :chained-map {:inc inc
                                         :dec dec}
                           :into-merge-map  {:into-edn/into {:a :overwritten-by-merge :d 3}
                                             :into-edn/merge #(into {}
                                                                    (for [v (range %)
                                                                          :let [k (-> v (+ 97) char
                                                                                      str keyword)]]
                                                                      [k v]))}
                           :chained-vec [#(range (* % %))
                                         [range
                                          inc]]}
                    :target 3
                    :result {:ident 3,
                             :inc 4,
                             :dec 2,
                             :range '(0 1 2),
                             :range-vec [0 1 2],
                             :range-inc [1 2 3],
                             :range-map [{:inc 1, :dec -1} {:inc 2, :dec 0} {:inc 3, :dec 1}],
                             :nil-vec [],
                             :chained-map {:inc 4, :dec 2},
                             :into-merge-map {:a 0, :b 1, :c 2, :d 3},
                             :chained-vec
                             [[]
                              [1]
                              [1 2]
                              [1 2 3]
                              [1 2 3 4]
                              [1 2 3 4 5]
                              [1 2 3 4 5 6]
                              [1 2 3 4 5 6 7]
                              [1 2 3 4 5 6 7 8]]
                             }}])

(deftest simple-test
(doseq [{:keys [title spec target result]} simple-tests]
  (testing title
    (is (= result 
          (into-edn spec target))))))



