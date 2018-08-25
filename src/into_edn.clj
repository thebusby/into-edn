(ns into-edn
  "A simple library to convert a Document/Object
   into EDN data via a spec.")

(def ^{:private true} this-ns (namespace ::_))

(defn actual-fn?
  "Returns true is argument is a function and not a collection posing as one."
  [f]
  (and (fn? f) (not (coll? f))))

(defprotocol IntoEdn
  (into-edn [spec target]
    "Produce EDN data via the provided spec and target.

   A spec is defined by maps, vectors, and functions;
   - A function returns the result of (spec-fn target).
     Note that keywords are not considered as functions
     but as literals that remain in place.
   - vector [seq-fn], or [seq-fn elem-fn], returns a
     vector of the results of (seq-fn target) where
     seq-fn should return a seq. If an elem-fn is provided,
     it is applied to each element of the sequence before
     being returned.
   - map returns a map where each value in the map is
     replaced when it's a spec (function, map, or vector).
     Two namespaced keywords, ::into and ::merge, allows the map to be
     respectively pre-populated and post-merged with the replacement
     of their values.

   Example:
   (def spec {:inc       inc
              :range     range
              :range-vec [range]
              :range-inc [range
                          inc]
              :range-map [range
                          {:inc inc
                           :dec dec
                           :untouched :literal}]
              :into-map  {:into-edn/into {:a :overwritten-by-merge :d 3}
                          :into-edn/merge #(into {}
                                                 (for [v (range %)
                                                       :let [k (-> v (+ 97) char
                                                                   str keyword)]]
                                                   [k v]))}})
   (into-edn spec 3)
   ;; => {:inc 4,
   ;;     :range (0 1 2),
   ;;     :range-vec [0 1 2],
   ;;     :range-inc [1 2 3],
   ;;     :range-map [{:inc 1,
   ;;                  :dec -1,
   ;;                  :untouched :literal}
   ;;                 {:inc 2,
   ;;                  :dec 0,
   ;;                  :untouched :literal}
   ;;                 {:inc 3,
   ;;                  :dec 1,
   ;;                  :untouched :literal}]
   ;;     :into-map  {:a 0, :b 1, :c 2, :d 3}}"))

(extend-protocol IntoEdn

  java.lang.Object
  (into-edn [spec target]
    spec)

  clojure.lang.IFn
  (into-edn [spec target]
    (if (actual-fn? spec)
      (spec target)
      spec))

  clojure.lang.APersistentVector
  (into-edn [[spec sub-spec] target]
    (let [elem-fn (if sub-spec
                    #(into-edn sub-spec %)
                    identity)]
      (mapv elem-fn (spec target))))

  clojure.lang.APersistentMap
  (into-edn [spec target]
    (let [res (persistent! (reduce (fn [agg [k v]]
                                     (if (= this-ns (namespace k))
                                       agg
                                       (assoc! agg k (into-edn v target))))
                                   (transient (if-let [into-spec (::into spec)]
                                                (into-edn into-spec target)
                                                {}))
                                   spec))]
      (if-let [merge-spec (::merge spec)]
        (merge res (into-edn merge-spec target))
        res)))

  )
