(ns amazonica.examples.simpleworkflow.dev
  (:require [amazonica.aws.simpleworkflow :as swf]
            [amazonica.examples.simpleworkflow.app :as app]
            [amazonica.examples.simpleworkflow.wfapp :as wfapp]
            [dire.core :as dire]))

(def cred {:access-key (System/getenv "AWS_ACCESS_KEY_ID")
           :secret-key (System/getenv "AWS_SECRET_ACCESS_KEY")
           :endpoint   "us-west-2"})
