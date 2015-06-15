# Service Registry

Clients of a service user either [client side discovery](client-side-discovery.md) or [service side discovery](server-side-discovery.md) to determine the location of a service instance to send the requests. By providing a service registry, which is a database of service instances and their locations, the discovery mechanisms are able to find the available instances of a service.

The benefits of the service registry pattern is that the client of the service and/or [message router](eip/message-router.md) can discover the location of service instances.
There are also some drawbacks; Unless part of the cloud environment, the router is another system component that must be installed and configured. Moreover the service registry is a critical component. Although clients should [cache](cache.md) data provided by the service registry, if the service registry fails that data will eventually become outdated. Consequently the service registry must be highly available.

Beside the service registry one also need to decide how service instances are registered with the service registry. There are two options:

1. [Self registration pattern](self-registration.md) - service instances register themselves.
2. [3rd party registration pattern](third-party-registration.md) - a 3rd party registers the service instances with the service registry.

The clients of the service registry need to know the location(s) of the service registry instances. THis implicitly means that service registry instances must be deployed on fixed and well known locations. Clients are configured with those locations.

Examples of service registries include:

* [Eureka](https://github.com/Netflix/eureka/wiki/Eureka-at-a-glance)
* [Zookeeper](http://zookeeper.apache.org/)
* [Consul](https://consul.io/)

# Related patterns

* [Client side -](client-side-discovery.md) and [server side discovery](server-side-discovery.md) create the need for a service registry.
* [self registration](self-registration.md) and [3rd-party-registration](third-party-registration.md) are two different ways that service instances can be registered with the service registry.

