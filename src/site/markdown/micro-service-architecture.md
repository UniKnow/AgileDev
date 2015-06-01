# Micro service architecture

Within a micro service architecture the functionality is decomposed in a set of collaborating services and the [scale cube](scale-cube.html) is applied. Services communicate with each other either using synchronous protocols like HTTP/REST or asynchronous protocols such as AMQP/JMS.

Services are developed independently from another.

Each service has its own storage in order to decouple from other services. When necessary, consistency between services is maintained using application events.

This approach has a number of benefits:

* Each service is relatively small
    * Easier for developers to understand.
    * The web container starts fast, which makes developers more productive and speed up deployments.
* Each service can be deployed independently of other services; e.g. easier to deploy new versions of services frequently.

...

## Patterns

* [Monolithic Architecture](monolithic-architecture.html) (Anti pattern).



