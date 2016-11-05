(ns clojusc.aws.examples.util)

(defn get-metas
  ""
  [an-ns]
  (->> an-ns
       (ns-publics)
       (map (fn [[k v]] [k (meta v)]))
       (into {})))

(defn get-meta
  "Takes the same form as the general `get-in` function:

      (get-meta 'my.name.space ['my-func :doc])"
  [an-ns rest]
  (-> an-ns
      (get-metas)
      (get-in rest)))

(defn get-docstring
  ""
  [an-ns fn-name]
  (get-meta an-ns [fn-name :doc]))
