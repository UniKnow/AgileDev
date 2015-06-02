# Micro service architecture

Within a micro service architecture the functionality is decomposed in a set of collaborating services and the [scale cube](scale-cube.html) is applied. Services communicate with each other either using synchronous protocols like HTTP/REST or asynchronous protocols such as AMQP/JMS.

Services are developed independently from another.

Each service has its own storage in order to decouple from other services. When necessary, consistency between services is maintained using application events.

This approach has a number of benefits:

* Each service is relatively small
    * Easier for developers to understand.
    * The web container starts fast, which makes developers more productive and speed up deployments.
* Each service can be deployed independently of other services; e.g. easier to deploy new versions of services frequently.
* Easier to scale development. It enables to organize the development effort around multiple teams. Each team can develop, deploy, and scale their service independently of all other teams.
* Improved fault isolation.
* Each service can be developed and deployed independently.
* Eliminates long term commitment to a technology stack.

There are however also a number of drawbacks:

* Developers must deal with the additional complexity of creating distributed systems.
    * Testing is more difficult.
    * Developers must implement the inter-service communication mechanism
    * Implementing use cases that span multiple services without using distributed transactions is difficult (See [Try-Cancel/Confirm](tcc.md))
    * Implementing uses cases that span multiple services requires careful coordination between the teams.
* Deployment complexity in production; there is additional operational complexity of deploying and managing a system comprised of many different services.
* Increased memory consumption. The micro service architecture replaces a monolithic application with N service instances. If each service runs in its own JVM (which is usually necessary to isolate instances) there is a overhead of N-1 JVM runtimes. Moreover, if each service runs on its own VM the overhead is even higher.

A challenge is deciding how to partition the system into micro services. One approach is to partition services by use case. We could for example have a micro service shipping within a partitioned e-commerce application that is responsible for shipping completed orders.
Another approach is to partition by [entity](ddd/entities.md). This kind of service is responsible for all operations that operate on entities of a given type.

Ideally, each service should have only a small set of responsibilities. The [Single Responsible Principle](srp.html) states that every class should have responsibility over a single part of the functionality provided by the software, and that responsibility should be entirely encapsulated by the class. It makes sense to apply this principle to micro service design as well.

## TODO

* Check whether OSGI could help with decreasing memory consumption.

## Related Patterns

* [API Gateway](API-gateway) - defines how clients access the services in a micro service architecture.
* [Client side discovery](client-side-discover) and [Service side discovery](server-side-discover) - Patterns used to route requests for a client to an available micro service.
* [Command Query Response Seperation](cqrs.md) - Helps develop scalable, extensible and maintainable applications.
* [Domain Driven Design](ddd/introduction_ddd.md) - Set of patterns for building enterprise applications based on the domain model.
* [Monolithic Architecture](monolithic-architecture) - alternative to the monolithic architecture.
* [Service Connector](service-connector.md) - Provide high level interface that hides implementation details regarding communication, thereby making the use of the micro service easier.
* [Service Statelessness](service-statelessness) - To have services scalable we should attempt to make them statelessness.

## See also

* [JBOSS OSGI User guide](https://docs.jboss.org/author/display/JBOSGI/User+Guide) - Guide explaining how to deploy application as OSGI bundle on JBOSS server.



