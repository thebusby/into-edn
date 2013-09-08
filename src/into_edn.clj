(ns into-edn
  "A simple library to convert a Document/Object
   into EDN data via a spec.")

(defprotocol IntoEdn
  (into-edn [spec target]
    "Produce EDN data via the provided spec and target.

   A spec is defined by maps, vectors, and fns;
   - An fn returns the result of (spec-fn target).
   - vector [seq-fn], or [seq-fn elem-fn], returns a
     vector of the results of (seq-fn target) where
     seq-fn should return a seq. If an elem-fn is provided,
     it is applied to each element of the sequence before
     being returned.
   - map returns a map where each value in the map is
     replaced.

   Example:
   (def spec {:inc       inc
              :range     range
              :range-vec [range]
              :range-inc [range
                          inc]
              :range-map [range
                          {:inc inc
                           :dec dec}]})
   (into-edn spec 3)"))

(extend-protocol IntoEdn

  clojure.lang.IFn
  (into-edn [spec target]
    (spec target))

  clojure.lang.APersistentVector
  (into-edn [[spec sub-spec] target]
    (let [elem-fn (if sub-spec
                    #(into-edn sub-spec %)
                    identity)]
    (vec (map elem-fn (spec target)))))

  clojure.lang.APersistentMap
  (into-edn [spec target]
    (persistent! (reduce (fn [agg [k v]]
                           (assoc! agg k (into-edn v target)))
                         (transient {})
                         spec)))

  )
