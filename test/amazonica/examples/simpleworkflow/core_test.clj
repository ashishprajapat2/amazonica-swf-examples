(ns amazonica.examples.simpleworkflow.core-test
  (:require [clojure.test :refer :all]
            [amazonica.examples.simpleworkflow.core :as sws]))

(deftest a-test
  (is (= (sws/foo "bar")
         ["bar" "Hellow, World!"])))
