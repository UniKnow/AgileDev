# Self Registration

Service instances must be registered with the [service registry](service-registry.md) on startup so that they can be discovered and unregistered on shutdown.

Registration of the service should happen with the service registry on startup and unregister on shutdown. If a service instance crashes the service must be unregistered from the service registry. The same applies for service instances that are running but are incapable of handling requests.

Within the self registration solution, a service instance is responsible for registering itself with the service registry. On startup the service instance register itself (location) with the service registry and makes itself available for discovery. The service instance must periodically renew it's registration so that the registry knows it is still alive.

[Eureka](https://github.com/Netflix/eureka/wiki/Understanding-eureka-client-server-communication) is an example of a service registry which provides a service registry API and a client library that service instances use to (un)register themselves.

The benefit of the self registration pattern is that a service instance knows best it's own state so a state model that's more complex than `UP`/`DOWN` should be possible.

There are also a couple of drawbacks:

* Couples the service to the service registry
* Service registration logic must be implemented for all languages that you are using within your environment/client.
* A service which is running but unable to process requests will often lack the self awareness to unregister from the service registry.

## Related patterns

* [Active monitoring](active-monitoring.md) - if a service crashes it might be that service is not properly unregistered. By actively monitoring the environment we might detect this and remove service instance from registry.
* [Service registry](service-registry.md) - registry at which service instance registers itself, and un-registers when service is shutdown, crashes, or becomes unavailable.
* [3rd party registration](third-party-registration.md) - alternative method to register services.
