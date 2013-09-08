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

                   {:title "The whole shebang"
                    :spec {:inc inc
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
                           :chained-vec [#(range (* % %))
                                         [range
                                          inc]]
                           :ident identity}
                    :target 3
                    :result {:chained-map {:inc 4, :dec 2},
                             :inc 4,
                             :range '(0 1 2),
                             :chained-vec
                             [[]
                              [1]
                              [1 2]
                              [1 2 3]
                              [1 2 3 4]
                              [1 2 3 4 5]
                              [1 2 3 4 5 6]
                              [1 2 3 4 5 6 7]
                              [1 2 3 4 5 6 7 8]],
                             :nil-vec [],
                             :dec 2,
                             :range-vec [0 1 2],
                             :range-map [{:inc 1, :dec -1} {:inc 2, :dec 0} {:inc 3, :dec 1}],
                             :range-inc [1 2 3],
                             :ident 3}}])

(deftest simple-test
(doseq [{:keys [title spec target result]} simple-tests]
  (testing title
    (is (= result 
          (into-edn spec target))))))



