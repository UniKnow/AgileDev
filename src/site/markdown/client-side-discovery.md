# Client Side Discovery

Services typically need to call one another. In a [monolithic](monolithic-architecture.md), services invoke one another through direct calls. In a traditional distributed system deployment, services run at fixed, well known locations (host, port, etc) and can easily call one another using HTTP/REST or some RPC mechanism. A modern micro service based application typically runs in a virtualized or containerized environment where the number of instances of a service and their locations change dynamically. As a consequence of this, you must implement a mechanism that enables clients of services to make requests to a dynamically changing set of service instances.

To overcome this the client obtains the location of a service instance by querying a [Service Registry](service-registry.md) which knows the locations of all service instances.

Disadvantage of this approach is that it couples the client to the service registry and client side service discovery logic needs to be implemented for each language/framework used within the client.

## Related patterns

* [Service Connector](service-connector.md) - Logic that is required for obtaining the location of a service can be implemented within the service connector.
* [Service Registry](service-registry.md) - queried by client, containing locations of all service instances.
* [Server Side Discovery](server-side-registry.md) is an alternative solution for the lookup of services.

## See also

*  [Ribbon Client](https://github.com/Netflix/ribbon) is an HTTP client that queries Eureka to route HTTP requests to an available service instance.

