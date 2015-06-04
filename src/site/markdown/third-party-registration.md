# 3rd Party Registration

Service instances must be registered with the [service registry](service-registry.md) on startup so that they can be discovered and unregistered on shutdown.

Registration of the service should happen with the service registry on startup and unregister on shutdown. If a service instance crashes the service must be unregistered from the service registry. The same applies for service instances that are running but are incapable of handling requests.

Within the 3rd party registration a registrar is responsible for registering and un-registering a service instance with the service registry. When the service instance starts up, the registrar registers the service instance with the service registry and when the service shuts down the registrar un-registers the service from the service registry

The benefits of the 3rd party registration are:

* Service code is less complex than when using [self registration](self-registration.md) since it is not responsible for registering itself.
* The registrar can perform health checks on a service instance and register/un-register the instance based on the health check.

Drawbacks of 3rd party registrations are:

* The registrar might only have a high level view of the service instance state (`UP`/`DOWN`) and so might not know whether it can handle requests.
* Unless the registrar is part of the infrastructure, it's another component that must be installed, configured and maintained. Also, since it is a critical system component it need to be highly available.

## Related paterns

* [Service Registry](service-registry.md)
* [Client](client-side-discovery.md) and [server side discovery](server-side-discovery.md).
* [Self registration](self-registration.md) is an alternative solution.

