# amazonica-swf-examples

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]
[![Clojure version][clojure-v]](project.clj)

*AWS Simple Workflow Service Examples in Clojure (using Amazonica)*

[![Amazonica Logo][logo]][logo]


#### Contents

* [About](#about-)
* [Dependencies](#dependencies-)
* [Documentation](#documentation-)
* [Usage](#usage-)
* [License](#license-)


## About [&#x219F;](#contents)

This project provides a port of the official Java examples provided by Amazon
for its [Simple Workflow service](http://docs.aws.amazon.com/amazonswf/latest/awsflowguide/getting-started.html).
In particular, the "Getting Started" section of the docs has been presented
in this repo's documentation. The following sections are included:
 * HelloWorld Application
 * TBD: HelloWorldWorkflow Application
 * TBD: HelloWorldWorkflowAsync Application
 * TBD: HelloWorldWorkflowDistributed Application

See the "Documentation" section below for a link to all of these.


## Dependencies [&#x219F;](#contents)

* `lein`
* Java

All other deps are pulled down by `lein`.


## Documentation [&#x219F;](#contents)

Documentation for the Clojure ports of the Java AWS SWF examples is provided here:
 * https://clojusc.github.io/amazonica-swf-examples


## Usage [&#x219F;](#contents)

Start up the REPL:

```
$ lein repl
```

HelloWorld Application:


```clj
amazonica.examples.simpleworkflow.dev=> (app/-main)
Hello, World!
nil
```

HelloWorldWorkflow Application:


```clj

```

HelloWorldWorkflowAsync Application:


```clj

```

HelloWorldWorkflowDistributed Application:


```clj

```


## License [&#x219F;](#contents)

Copyright © 2016, Clojure-Aided Enrichment Center

Apache License, Version 2.0.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/amazonica-swf-examples
[travis-badge]: https://travis-ci.org/clojusc/amazonica-swf-examples.png?branch=master
[deps]: http://jarkeeper.com/clojusc/amazonica-swf-examples
[deps-badge]: http://jarkeeper.com/clojusc/amazonica-swf-examples/status.svg
[logo]: resources/images/claws.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/amazonica-swf-examples.svg?maxAge=2592000
[tag]: https://github.com/clojusc/amazonica-swf-examples/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/clojusc/amazonica-swf-examples
[clojars-badge]: https://img.shields.io/clojars/v/clojusc/amazonica-swf-examples.svg
