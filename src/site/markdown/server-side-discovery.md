# Server Side Discovery

Services typically need to call one another. In a [monolithic](monolithic-architecture.md), services invoke one another through direct calls. In a traditional distributed system deployment, services run at fixed, well known locations (host, port, etc) and can easily call one another using HTTP/REST or some RPC mechanism. A modern micro service based application typically runs in a virtualized or containerized environment where the number of instances of a service and their locations change dynamically. As a consequence of this, you must implement a mechanism that enables clients of services to make requests to a dynamically changing set of service instances.

Within a server side discovery approach a request to a service is going via a [message router](eip/message-router.md) that runs at a well known location. The router queries a service registry, which might be built into the router, and forward the request to an available service instance.

The [AWS Elastic Load Balancing](http://aws.amazon.com/elasticloadbalancing/) is an example of a server side discovery solution. A client makes HTTP(S) requests to the ELB, which load balances the traffic amongst a set of EC2 instances. Within the Amazone solution ELB also functions as a [service registry](service-registry.md). EC2 instances are either registered with the instance explicitly via an API call or automatically as part of an auto-scaling group.

Server side discovery has a number of benefits:

* Compared to [client side discovery](client-side-discovery.md), the client code is simpler since it does not have to deal with discovery. Instead a client simply makes a request to a router.

It however also has some drawbacks:

* Unless part of the cloud environment, the router is another system component that must be installed and configured. It will also need to be replicated for availability and capacity.
* More network hops are required compared to [client side discovery](client-side-discovery.md).

## Related patterns

* [Client side discovery](client-side-discovery.md) is an alternative solution.
* [Message Router](eip/message-router.md)
* [Service Registry](service-registry.md)

