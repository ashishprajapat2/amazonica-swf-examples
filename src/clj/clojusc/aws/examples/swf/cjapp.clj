(ns clojusc.aws.examples.swf.cjapp
  "This work didn't end up being useful: it was done before I realized that I
  wasn't going to be able to figure out how to build a Simpleworkflow app
  in Clojure using AspectJ. It seems that it might be necessary to use Eclipse
  to write Simpleworkflow apps in Java :-(

  Anyway, this code is going to stay, since it does provide a nice example of
  how to annotate interfaces in Clojure.

  All this being said, if you do have the generated Java code, you can ditch
  the hand-written Java and use this Clojure instead ... though that probably
  pegs out the pointless-o-meter."
  (:require [clojure.tools.logging :as log]
            [clojusc.aws.examples.util :as util]
            [clojusc.twig :as twig]
            [dire.core :refer [with-handler!]])
  (:import [clojusc.aws.examples.swf.javaapp
             GreeterActivitiesClientImpl
             GreeterWorkflowClientExternalFactoryImpl]
           [com.amazonaws ClientConfiguration]
           [com.amazonaws.auth
             AWSCredentials
             BasicAWSCredentials]
           [com.amazonaws.services.simpleworkflow
             AmazonSimpleWorkflow
             AmazonSimpleWorkflowClient]
           [com.amazonaws.services.simpleworkflow.flow
             ActivityWorker
             WorkflowWorker]
           [com.amazonaws.services.simpleworkflow.flow.core
             Promise]
           [com.amazonaws.services.simpleworkflow.flow.annotations
             Activities
             ActivityRegistrationOptions
             Execute
             Workflow
             WorkflowRegistrationOptions]
           [java.lang
             Override
             String])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Constants and setup ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def domain "clojusc.hello-world-workflow")
(def activity-type-name "hw-wf")
(def workflow-type-name "hw-wf")
(def task-list-poll "hw-wf-tl")
(def client-id "greeter-client-id")
(def app-api-version "2.0")
(def access-key (System/getenv "AWS_ACCESS_KEY_ID"))
(def secret-key (System/getenv "AWS_SECRET_KEY"))
(def region "us-west-2")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Protocols and behaviours ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;
;;; These are the exact same as what we defined in our prototype app that
;;; didn't use AWS at all.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(definterface
  ^{ActivityRegistrationOptions {
     :defaultTaskScheduleToStartTimeoutSeconds 300
     :defaultTaskStartToCloseTimeoutSeconds 10}
    Activities {
     :version "2.0"}}
  GreeterActivities
  (^String getName []
    "Return the greeter's name.")
  (^String getGreeting [value]
    "Given a value, usually a greeter's name, return a greeting.")
  (^void say [greeting]
    "Given a greeting, display it (usually to `stdout`)."))

(deftype GreeterActivitiesImpl []
  GreeterActivities
  (getName [this] "World")
  (getGreeting [this value] (format "Hello, %s!" value))
  (say [this greeting] (println greeting)))

(definterface
  ^{Workflow true
    WorkflowRegistrationOptions {
     :defaultExecutionStartToCloseTimeoutSeconds 3600}}
  GreeterWorkflow
  (^void ^{Execute {:version "2.0"}} greet []))

(deftype GreeterWorkflowImpl []
  GreeterWorkflow
  (^void greet [this]
    (let [operations (new GreeterActivitiesClientImpl)]
      (->> (.getName operations)
           (.getGreeting operations)
           (.say operations)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Run the workflow ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn usage
  "  Usage: lein cjapp <command>

  This AWS Simpleworkflow example requires that the example worker be running
  before the workflow is started. The simplest way to do this is open two
  terminal windows in this project directory, and execute each of the
  following (each in its own terminal):

  ```
  $ lein cjapp start worker
  $ lein cjapp run
  ```

  Command execution order is not actually important here. If `run` is done
  before `start worker`, it will just be put in the execution queue on AWS
  until `start worker` is run (or until the run request times out at AWS).

  Note that the worker will run continuously until its connection with AWS
  times out, whereas after the javaapp workflow is run, it will exit
  immediately."
  []
  (println)
  (-> 'clojusc.aws.examples.swf.cjapp
      (util/get-docstring 'usage)
      (println)))

(defn start-worker
  ""
  []
  (let [config (.withSocketTimeout (new ClientConfiguration) (* 70 1000))
        creds (new BasicAWSCredentials access-key secret-key)
        service (new AmazonSimpleWorkflowClient creds config)]
    (.setEndpoint service (format "https://swf.%s.amazonaws.com" region))
    (let [activity-worker (new ActivityWorker service domain task-list-poll)]
      (.addActivitiesImplementation activity-worker (new GreeterActivitiesImpl))
      (.start activity-worker))
    (let [workflow-worker (new WorkflowWorker service domain task-list-poll)]
      (.addWorkflowImplementationType workflow-worker GreeterWorkflowImpl)
      (.start workflow-worker))))

(defn run
  ""
  []
  (let [config (.withSocketTimeout (new ClientConfiguration) (* 70 1000))
        creds (new BasicAWSCredentials access-key secret-key)
        service (new AmazonSimpleWorkflowClient creds config)]
    (.setEndpoint service (format "https://swf.%s.amazonaws.com" region))
    (let [factory (new GreeterWorkflowClientExternalFactoryImpl service domain)
          greeter (.getClient factory client-id)]
      (.greet greeter))))

(defn -main
  "Run the workflow app."
  [& cmd]
  (twig/set-level! '[amazonica
                     clojusc
                     com.amazonaws] :debug)
  (case cmd
    ["start" "worker"] (start-worker)
    [:start :worker] (start-worker)
    ["run"] (run)
    [:run] (run)
    ["help"] (usage)
    ["--help"] (usage)
    ["-h"] (usage)))
