# SOA and DDD (in progress)

Within DDD blue book a service is introduced as a building block:

    When a significant process or transformation in the domain is not a natural responsibility of an Entity or Value Object, add an operation to the model as standalone interface declared as Service. Define the interface in terms of the ubiquitous language of the model and make sure the operation name is part of the ubiquitous language. Make the service stateless.

These type of services can be identified as domain services and are part of the domain layer. Domain services are different from infrastructural services because they embed and operate on domain concepts and are part of the ubiquitous language. Infrastructural services are instead focused on encapsulating the 'plumbing' requirements of an application; usually IO concerns such as file system access, database access, email, etc. For example, a common application requirement is the sending of and email notification informing interested parties about some event. The concept of the event exists in the domain layer and the domain layer determines when the event should be raised. An email infrastructural service can handle the domain event by generating and transmitting the appropriate email message. Another infrastructural service can handle the same event and send a SMS via another channel. The domain layer doesn't care about the specifics or how an event notification is delivered, it only cares about raising the event. Another example of an infrastructural service is the implementation of an repository. The interface is declared in the domain layer and is an important aspect of the domain, however the specifics of the communication with storage mechanisms are handled in the infrastructural layer.

An application service provides a hosting environment for the execution of domain logic. As such, it is a convenient point to inject various [gateways](http://martinfowler.com/eaaCatalog/gateway.html) such as a repository or wrappers for external services. A common problem in applying DDD is when an entity requires access to data in a repository or other gateway to carry out a business operation. One solution is to inject repository dependencies directly into the entity but that makes reasoning about the behaviour of entities more difficult since the '(single responsibility principle)[http://en.wikipedia.org/wiki/Single_responsibility_principle]' is violated.

In addition of being a host, the application service exposes the functionality of the domain to other application layers and an API. In this way, an application service also fulfills a translation role - that of translating between external commands and the underlying domain model. For example, a command can be something like `transfer $5 from account A to B`. There are a number of steps required to fulfill that command (`load account with ID A`, `load account with ID B`, `debit account A`, etc), and the application service is best suited for this job.

The differences between a domain service and an application service are subtle:

* Domain services are very granular where as application services are facade purposed providing an API.
* Domain services contain domain logic that can't naturally be placed in an entity or value object, whereas application services orchestrate the execution of domain logic and don't implement themselves any domain logic.
* Domain service methods have other domain elements as attributes and return values whereas application services operate upon trivial operands such as identify values and primitive data structures.
* Application services declare dependencies on infrastructural services required to execute domain logic.
* Command handlers of CQRS are a flavor of application services which focus on handling a single command.

In a complete application, a domain model does not stands on its own. Applications can have GUIs containing a presentation layer which facilitates between the user and the domain, or the application may wish to expose its functionality as a set of REST resource or SOA services. Application services form the API which orchestrate and delegate to the underlying entities, value objects and domain services.

## Resources

* [RESTful SOA or Domain-Driven Design - A Compromise?](http://www.infoq.com/presentations/RESTful-SOA-DDD)
* [Services in Domain Driven Design](http://gorodinski.com/blog/2012/04/14/services-in-domain-driven-design-ddd/)
* [Application Architecture Overview: layered, DDD, clean, SOA, and cloud-based architectures](https://www.youtube.com/watch?v=vw6YCt33P48)
* [Event-Driven Architecture (EDA), SOA, and DDD with Udi Dahan](https://vimeo.com/57644944)
* [Domain-Driven Design for RESTful Systems](https://yow.eventer.com/yow-2011-1004/domain-driven-design-for-restful-systems-by-jim-webber-1047)