![Conductor](docs/docs/img/conductor-vector-x.png)

## Documentation & Getting Started
[http://netflix.github.io/conductor/](http://netflix.github.io/conductor/)

[Getting Started](https://netflix.github.io/conductor/gettingstarted/basicconcepts/) guide.

Run `./gradlew build -x test && docker-compose up -d` to start conductor locally

## Get Conductor
Binaries are available from Maven Central and jcenter.

Below are the various artifacts published:

|Artifact|Description|
|-----------|---------------|
|conductor-common|Common models used by various conductor modules|
|conductor-core|Core Conductor module|
|conductor-redis-persistence|Persistence using Redis/Dynomite|
|conductor-es5-persistence|Indexing using Elasticsearch 5.X|
|conductor-jersey|Jersey JAX-RS resources for the core services|
|conductor-ui|node.js based UI for Conductor|
|conductor-contribs|Optional contrib package that holds extended workflow tasks and support for SQS|
|conductor-client|Java client for Conductor that includes helpers for running a worker tasks|
|conductor-server|Self contained Jetty server|
|conductor-test-harness|Used for building test harness and an in-memory kitchensink demo|

## Building
To build the server, use the following dependencies in your classpath:

* conductor-common
* conductor-core
* conductor-jersey
* conductor-redis-persistence (_unless using your own persistence module_)
* conductor-es5-persistence (_unless using your own index module_)
* conductor-contribs (_optional_)


### Deploying Jersey JAX-RS resources
Add the following packages to classpath scan:

```java
com.netflix.conductor.server.resources
com.netflix.workflow.contribs.queue
```
Conductor relies on the guice (4.0+) for the dependency injection.
Persistence has a guice module to wire up appropriate interfaces:

```java
com.netflix.conductor.dao.RedisWorkflowModule
```
## Database Requirements

* The default persistence used is [Dynomite](https://github.com/Netflix/dynomite)
* For queues, we are relying on [dyno-queues](https://github.com/Netflix/dyno-queues)
* The indexing backend is [Elasticsearch](https://www.elastic.co/) (5.x)

## Other Requirements
* JDK 1.8+
* Servlet Container
