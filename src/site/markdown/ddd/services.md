# Services.

Some concepts from the domain aren't natural to model as objects. Forcing the required domain functionality to be the responsibility of an `Entity` or `Value Object` either distorts the definition of a model based object or adds meaningless artificial objecs.

*Therefore*, when a significant process or transformation in the domain is not a natural responsibility of an `Entity` or `Value Object`, add an operation to the model as a standalone interface declared as a `Service`. Define the interface in terms of the language of the model and make sure the operation name is part of the ubiquitous language. Make the service stateless.

## Service types.

Services exist in most layers of the DDD layered architecture:

* Application
* Domain
* Infrastructure

An infrastructure service would be something that communicates directly with external resources, such as file systems, databases, etc.

Domain services are the coordinators, allowing higher level functionality between many different smaller parts. Since domain services are first-class citizens of the domain model, their names and usage should be part of the ubiquitous language. Meanings and responsibilities should make sense to the stakeholders or domain experts.

The application service will be acting as façade and accepts any request from clients. Once the request comes, based on the operation, it may call a Factory to create domain object, or calls repository service to re-create existing domain objects. Conversion between, DTO (Data Transfer Object) to domain objects and Domain objects to DTO will be happening here. Application service can also call another application service to perform additional operations.

The differences between a domain service and an application service are subtle but critical:

* Domain services are very granular where as application services are a facade with as purpose providing an API.
* Domain services contain domain logic that can't naturally be placed in an entity or value object, whereas application services orchestrate the execution of domain logic and don't themselves implement any domain logic.
* Domain service methods can have other domain elements as operands and return values whereas application services operate upon trivial operands such as identity.
* Application services declare dependencies on infrastructural services required to execute domain logic.
* Command handlers are a flavor of application services which focus on handling a single command typically in a CQRS architecture.