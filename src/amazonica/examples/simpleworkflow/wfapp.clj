(ns amazonica.examples.simpleworkflow.wfapp
  (:require [amazonica.aws.simpleworkflow :as swf]
            [amazonica.core :refer [defcredential]]
            [clojure.tools.logging :as log]
            [clojusc.twig :as twig]
            [dire.core :refer [with-handler!]])
  (:import [com.amazonaws.services.simpleworkflow.model
            DomainAlreadyExistsException
            TypeAlreadyExistsException])
  (:gen-class))

(def domain "clojusc.hello-world-workflow")
(def activity-type-name "hw-wf")
(def workflow-type-name "hw-wf")
(def version "1.0")

(defcredential
  (System/getenv "AWS_ACCESS_KEY_ID")
  (System/getenv "AWS_SECRET_ACCESS_KEY")
  "us-west-2")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Registration functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;
;;; The following are wrapped in functions simply in order to provide a neat
;;; call in the `-main` function below (we could just as easily add `dire`
;;; exception handlers to the `swf/*` functions as our wrappers). This just
;;; makes it nicer to read in the `(-main)` function.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn register-domain
  ""
  []
  (swf/register-domain
    :name domain
    :workflow-execution-retention-period-in-days "30"))

(defn register-activity-type
  ""
  []
  (swf/register-activity-type
    :domain domain
    :name activity-type-name
    :version version
    :default-task-schedule-to-start-timeout-seconds "300"
    :default-task-start-to-close-timeout-seconds "10"))

(defn register-workflow-type
  ""
  []
  (swf/register-workflow-type
    :domain domain
    :name workflow-type-name
    :version version
    :default-execution-start-to-close-timeout-seconds "3600"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Protocols and behaviours ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;
;;; These are the exact same as what we defined in our prototype app that
;;; didn't use AWS at all.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol IGreeterActivities
  "I am a sample interface that defines specific workflow activities for a
  'greeter'."
  (get-name [this]
    "Return the greeter's name.")
  (get-greeting [this value]
    "Given a value, usually a greeter's name, return a greeting.")
  (say [this greeting]
    "Given a greeting, display it (usually to `stdout`)."))

(defrecord GreeterActivities [])

(def greeter-actvities-behaviour
  "Implementation for `IGreeterActivities`."
  {:get-name (fn [this] "World")
   :get-greeting (fn [this value] (format "Hello, %s!" value))
   :say (fn [this greeting] (println greeting))})

(extend GreeterActivities IGreeterActivities greeter-actvities-behaviour)

(defprotocol IGreeterWorkflow
  "I am a sample interface that defines the entrypoint for a 'greeter'
  workflow."
  (greet [this]
    "This is the entrypoint function that kicks off a given workflow."))

(defrecord GreeterWorkflow [])

(def greeter-workflow-behaviour
  "Implementation for `IGreeterWorkflow`."
  {:greet (fn [this]
            (let [operations (new GreeterActivities)]
              (->> (get-name operations)
                   (get-greeting operations)
                   (say operations))))})

(extend GreeterWorkflow IGreeterWorkflow greeter-workflow-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Exception handlers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(with-handler!
  #'register-domain
  DomainAlreadyExistsException
  (fn [& rest]
    (log/warnf "The domain '%s' has already been registered." domain)))

(with-handler!
  #'register-activity-type
  TypeAlreadyExistsException
  (fn [& rest]
    (log/warnf "The activity type '%s' has already been registered."
               activity-type-name)))

(with-handler!
  #'register-workflow-type
  TypeAlreadyExistsException
  (fn [& rest]
    (log/warnf "The workflow type '%s' has already been registered."
               workflow-type-name)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Run the workflow ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -main
  [& args]
  "Run the workflow app."
  (twig/set-level! '[amazonica] :debug)
  ;; Perform workflow registrations, using our little wrappers
  (register-domain)
  (register-activity-type)
  (register-workflow-type)
  ;; Run some utility functions
  (log/debug "Open workflow operations:" (swf/describe-domain :name domain))
  (swf/start-workflow-execution
    :domain domain
    :workflow-id )
  (let [a 1]))
