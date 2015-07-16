# Canary endpoint monitoring

It is good practice - and often business requirement - to monitor web applications, middle-tier and shared services, to ensure that they are available and performing correctly. There are many factors that affect applications such as network latency, performance and availability of the underlying hardware and storage systems, and the network bandwidth between them. The service may fail entirely or partly due to any of these factors. Therefore, you must verify at regular intervals that the service is performing correctly to ensure the required level of availability.

To provide the functionality by which the application can be monitored one should implement an endpoint that performs the necessary checks and return an indication of its status. Such an endpoint, unlike other resources of a REST API, perform no business activity, but instead gathers status and latency of all dependencies of a service.

Checks that might be carried out by the monitoring code in the application include but not limited to:

* Checking storage or database for availability (and response time).
* Checking external distributed caches.
* Checking availability of Service bus
* Checking other resources or services used by the application.

The response code indicates the overall status of the application. If any of the dependencies has a non-success code, the returned status code will be non-success also. The payload contains the name of the dependencies it uses, and the status code that was returned by the canary endpoint of that particular service.

Implementation of the canary endpoint check is generally dependent on the underlying technology. For a cache service, it suffices to set a constant value and see it succeeding. For a SQL database a `select 1;` query is all that is needed. *Note that none of these are anywhere near a business activity, so that you could not, think that its success means your business is up and running.*
A canary endpoint normally gets implemented as an HTTP `GET` call which returns a collection of connectivity check metrics.

Typical checks that should be performed by the monitoring tool include:

* Validating the response code. For example, an HTTP response of 200 (OK) indicates that the services dependencies are all ok.
* Checking the content of the response to detect errors, even when a 200 (OK) status code is returned.

## Security of the canary endpoint.

Canary endpoints should be secured. A canary endpoint lists all internal dependencies, and potentially technologies of a system and this could be abused by hackers to target your system.

## Performance impact.

Since canary endpoints do not trigger any business activity, its performance footprint should be minimal. However, since calling the canary endpoint generates a cascade of calls, calling all canary endpoints every few seconds might not be wise.

## Related Patterns

* [Circuit breaker](circuit-breaker) - Based on the status code as exposed by the canary endpoint the circuit breaker can allow requests to the service or block.

## See also

* [Spring Boot custom HealthIndicator](http://www.jayway.com/2014/07/22/spring-boot-custom-healthindicator/)
* [AppDynamics](https://docs.appdynamics.com/display/DASH/Learn+AppDynamics) for monitoring tool.