(ns amazonica.examples.simpleworkflow.core-test
  (:require [clojure.test :refer :all]
            [amazonica.examples.simpleworkflow.core :as sws]))

(deftest ^:unit a-test
  (is (= (sws/foo "bar")
         ["bar" "Hello, World!"])))
