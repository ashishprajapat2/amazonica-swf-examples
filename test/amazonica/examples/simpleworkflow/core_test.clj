(ns amazonica.examples.simpleworkflow.core-test
  (:require [clojure.test :refer :all]
            [amazonica.examples.simpleworkflow.core :as swf]))

(deftest ^:unit a-test
  (is (= (swf/foo "bar")
         ["bar" "Hello, World!"])))
