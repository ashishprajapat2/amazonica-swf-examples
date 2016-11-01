(ns amazonica.examples.simpleworkflow.app
  (:gen-class))

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

(defn -main
  [& args]
  "Run the workflow app."
  (-> (new GreeterWorkflow)
      (greet)))
