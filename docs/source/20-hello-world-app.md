# HelloWorld Application

Adapted from the [Java HelloWorld Application](http://docs.aws.amazon.com/amazonswf/latest/awsflowguide/getting-started-example-helloworld.html).

To print "Hello, World!" to the console, the activity tasks must execute in
sequence in the correct order with the correct data.

## HelloWorld Activities Protocol

HelloWorld breaks the overall task of printing a "Hello World!" greeting to
the console into three tasks, each of which is performed by an activity
method. The activity methods are defined in the `IGreeterActivities`
protocol, as follows.

```clj
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

## HelloWorld Activities Implementation

HelloWorld has one activity implementation, `GreeterActivities`, which
provides the behaviour as shown:

```clj
(def greeter-actvities-behaviour
  "Implementation for `IGreeterActivities`."
  {:get-name (fn [this] "World")
   :get-greeting (fn [this value] (format "Hello, %s!" value))
   :say (fn [this greeting] (println greeting))})
```

## HelloWorld Workflow Worker

The HelloWorld workflow worker has a single method, the workflow's entry
point, which is defined in the IGreeterWorkflow protocol, as follows:

```clj
(defprotocol IGreeterWorkflow
  "I am a sample interface that defines the entrypoint for a 'greeter'
  workflow."
  (greet [this]
    "This is the entrypoint function that kicks off a given workflow."))
```

`GreeterWorkflow` implements this interface, as follows:

```clj
(def greeter-workflow-behaviour
  "Implementation for `IGreeterWorkflow`."
  {:greet (fn [this]
            (let [operations (new GreeterActivities)]
              (->> (get-name operations)
                   (get-greeting operations)
                   (say operations))))})
```


### HelloWorld Workflow Starter

A workflow starter is an application that starts a workflow execution, and
might also communicate with the workflow while it is executing. The
`amazonica.examples.simpleworkflow.app` namespace implements the HelloWorld
workflow starter, as follows:

```clj
(defn -main
  [& args]
  "Run the workflow app."
  (-> (new GreeterWorkflow)
      (greet)))
```
