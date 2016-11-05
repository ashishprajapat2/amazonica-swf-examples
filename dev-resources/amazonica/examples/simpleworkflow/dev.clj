(ns amazonica.examples.simpleworkflow.dev
  (:require [amazonica.aws.simpleworkflow :as swf]
            [amazonica.examples.simpleworkflow.app :as app]
            [clojusc.aws.examples.swf.cjapp :as cjapp]
            [clojusc.aws.examples.swf.javaapp.run :as javaapp]
            [dire.core :as dire])
  (:import [clojusc.aws.examples.swf.javaapp
             GreeterActivitiesClientImpl
             GreeterWorkflowClientExternalFactoryImpl]))

(def cred {:access-key (System/getenv "AWS_ACCESS_KEY_ID")
           :secret-key (System/getenv "AWS_SECRET_ACCESS_KEY")
           :endpoint   "us-west-2"})
