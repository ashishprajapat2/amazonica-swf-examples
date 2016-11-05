(ns clojusc.aws.examples.swf.javaapp.run
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as twig]
            [dire.core :refer [with-handler!]]
            [clojusc.aws.examples.util :as util])
  (:import [clojusc.aws.examples.swf.javaapp
             GreeterWorker
             GreeterMain])
  (:gen-class))

(defn usage
  "  Usage: lein javaapp <command>

  This AWS Simpleworkflow example requires that the example worker be running
  before the workflow is started. The simplest way to do this is open two
  terminal windows in this project directory, and execute each of the
  following (each in its own terminal):

  ```
  $ lein javaapp start worker
  $ lein javaapp run
  ```

  Command execution order is not actually important here. If `run` is done
  before `start worker`, it will just be put in the execution queue on AWS
  until `start worker` is run (or until the run request times out at AWS).

  Note that the worker will run continuously until its connection with AWS
  times out, whereas after the javaapp workflow is run, it will exit
  immediately."
  []
  (println)
  (-> 'clojusc.aws.examples.swf.javaapp.run
      (util/get-docstring 'usage)
      (println)))

(defn -main
  [& cmd]
  "Run the workflow app."
  (twig/set-level! '[amazonica
                     clojusc
                     com.amazonaws] :debug)
  (case cmd
    ["start" "worker"] (GreeterWorker/main)
    [:start :worker] (GreeterWorker/main)
    ["run"] (GreeterMain/main)
    [:run] (GreeterMain/main)
    ["help"] (usage)
    ["--help"] (usage)
    ["-h"] (usage)))
