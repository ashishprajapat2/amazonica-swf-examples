# HelloWorldWorkflow Application

Adapted from the [Java HelloWorldWorkflow Application](http://docs.aws.amazon.com/amazonswf/latest/awsflowguide/getting-started-example-helloworldworkflow.html).

## Registering the Workflow Domain


```clj
(def domain "hello-world-workflow")
(def version "1.0")

(swf/register-domain
  :name domain
  :workflow-execution-retention-period-in-days "30")
```


## HelloWorldWorkflow Activities Worker Protocol

HelloWorldWorkflow defines the activities interface in `IGreeterActivities`, as
follows:

```clj
(swf/register-activity-type
  :domain domain
  :name "hw-wf"
  :version version
  :default-task-schedule-to-start-timeout-seconds "300"
  :default-task-start-to-close-timeout-seconds "10")

(defprotocol IGreeterActivities
  "I am a sample interface that defines specific workflow activities for a
  'greeter'."
  (get-name [this]
    "Return the greeter's name.")
  (get-greeting [this value]
    "Given a value, usually a greeter's name, return a greeting.")
  (say [this greeting]
    "Given a greeting, display it (usually to `stdout`)."))
```


## HelloWorldWorkflow Activities Worker Implementation

HelloWorldWorkflow implements the activity methods in `GreeterActivities`, as
follows:

```clj
(defrecord GreeterActivities [])

(def greeter-actvities-behaviour
  "Implementation for `IGreeterActivities`."
  {:get-name (fn [this] "World")
   :get-greeting (fn [this value] (format "Hello, %s!" value))
   :say (fn [this greeting] (println greeting))})

(extend GreeterActivities IGreeterActivities greeter-actvities-behaviour)
```

Notice that the code is identical to the HelloWorld implementation. At its
core, an AWS Flow Framework activity is just a method that executes some code
and perhaps returns a result. The difference between a standard application
and an Amazon SWF workflow application lies in how the workflow executes the
activities, where the activities execute, and how the results are returned to
the workflow worker.


## HelloWorldWorkflow Workflow Worker

## HelloWorldWorkflow Workflow and Activities Implementation

## HelloWorldWorkflow Starter
