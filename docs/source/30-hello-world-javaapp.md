# HelloWorldWorkflow - Java Application

## Motivation

This project provides a working Java application so that one may easily sanity
check one's own Clojure application against a known entity: the standard SWF
example.


## Usage

To run the Java version of the sample SWF application, open up two terminals
whose working directory is that of this project's clone, and then run these
two commands (one in each terminal):

```
$ lein javaapp start worker
```

```
$ lein javaapp run
```


## Details

This Java workflow example application is taken directly from the
[AWS Simpleworkflow Developer Guide](http://docs.aws.amazon.com/amazonswf/latest/awsflowguide/getting-started-example-helloworldworkflow.html).
However ... it's a bit trickier than that. As the
[setup instructions]()
for that example application intimate, it's basically required to use Eclipse
and a series of libraries and Eclipse plugins in order to develop a
Simpleworkflow application in Java. Some of the needed bits are:
* AWS JDK
* SWF Build Tools
* AspectJ
* AspectJ Weaver

The reason for these is that some of the code that you write for a Java SWF
application isn't actually used, but is rather used to generate other code
that *is* used. Various interface and method annotations are used to
accomplish this.

Generating the required code may be possible from the commandline without using
Eclipse, but I wasn't able to figure it out. Furthermore, getting Eclipse to
actually generate the annotations-based code was totally non-trivial (which
may be to having different versions of libraries and Eclipse itself than the
AWS instructions). In the end, I was only able to genereate them "accidentally"
(I discovered that clicking the "Reindex" button in the
"Preferences -> JDT Weaving" configuration pane produced the generated files in
the expected `.apt_generated` directory).

As such, the generated files are included here in the example source code, in
the `src/java/clojusc/aws/examples/swf/javaapp` directory. The
files that were written by hand are the following:

* `GreeterActivities.java`
* `GreeterActivitiesImpl.java`
* `GreeterConstants.java`
* `GreeterMain.java`
* `GreeterWorker.java`
* `GreeterWorkflow.java`
* `GreeterWorkflowImpl.java`

