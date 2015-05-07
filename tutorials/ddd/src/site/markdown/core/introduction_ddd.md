# Introduction Domain Driven Design

The philosophy of Domain Driven Design (DDD), firstly described by Eric Evans, is about placing our attention at the heart of the application, focusing on the complexity that is intrinsic to the business domain itself.

Domain Driven Design consists of a set of [patterns](building_blocks_ddd.html) for building enterprise applications based on the domain model. Within this introduction we are going to run through some of concepts and terminology of DDD.

## Domain Model

At the heart of DDD lies the concept of the domain model. This model is built by the team responsible for developing the system. The team consists of both domain experts from the business and software developers. The role of the domain model is to capture what is valuable or unique to the business. The domain model serves the following functions:

* It captures the relevant domain knowledge from the domain experts.
* It enables the team to determine the scope and verify the consistency of the knowledge.
* The model is expressed in code by the developers.
* It is constantly maintained to reflect evolutionary changes in the domain.

Domain models are typically composed of elements such as [entities](entities.html), [value objects](value_objects.html), [aggregates](aggregates.html), and described using terms from a ubiquitous language.

## Ubiquitous language

The ubiquitous language is very closely related to the domain model. One of the functions of the domain model is to create a common understanding between the domain experts and the developers. By having the domain experts and developers use the same terms of the domain model for objects and actions within the domain, the risk of confusion or misunderstanding is reduced.

## Entities, value objects and services

DDD uses the following concepts to identify some of the building blocks that will make up the domain model:

* [Entities](entities.html) are objects that are defined by their identity and that identity continuous through time.
* [Value objects](value_object.html) are objects which are not defined by their identity. Instead they are defined by the values of their attributes.
* [Services](services.html) is a collection of stateless methods that doesn't model properly to a entity or value object.

## Aggregates and aggregate roots

DDD uses the term [aggregate](aggregates.html) to define a cluster of related entities and value objects that form a consistency boundary within the system. That consistency boundary is usually based on transactional consistency.
The aggregate root (also known as root entity) is the gatekeeper object for the aggregate. All access to the objects within the aggregate must occur through the aggregate root; external entities are only permitted to hold a reference to the aggregate root.
In summary, aggregates are mechanism that DDD uses to manage the complex set of relationships between the many entities and value objects in a typical domain model.

## Bounded context

For a large system, it may not be practical to maintain a single domain model; the size and complexity would make it difficult to keep it consistent. To address this, DDD introduces the concept of [bounded context](bounded_context.html) and multiple models. Within a system, you might choose to use multiple smaller models rather than a single large model, each focusing on a aspect or grouping of functionality within the overall system. A bounded context is the context for one particular domain model. Each bounded context has its own ubiquitous language, or at least its own dialect of the domain ubiquitous language.

### Anti corruption layers

Different bounded contexts have different domains models. When your bounded contexts communicate with each other, you need to ensure that concepts specific to one domain do not leak into another domain model. An anti corruption layer functions as a gatekeeper between bounded contexts and helps you to keep the domain models clean.

### Context maps

A large complex system can have multiple bounded contexts that interact with each other in various ways.
A business entity, such as a customer, might exist in several bounded contexts. However, it may need to expose different facets or properties that are relevant to a particular bounded context. As a customer entity moves from one bounded context to another, you may need to translate it so that is exposes the relevant facets or properties for that particular context. A context map is the documentation that describes the relationships between these bounded contexts.

### Bounded contexts and multiple architectures

Following the DDD approach, the bounded context will have its own domain model and its own ubiquitous language. Bounded contexts are also typically vertical slices through the system, meaning that the implementation of a bounded context will include everything from the data store, right up to the UI.
One important consequence of this split is that you can use different implementation architectures in different bounded contexts. Due to this you can use an appropriate technical architecture for different parts of the system to address its specific characteristics.

### Bounded contexts and multiple development teams

Clearly separating bounded contexts, and working with separate domain models and ubiquitous languages makes it possible to develop bounded contexts in parallel.

### Maintaining multiple bounded contexts

It is unlikely that each bounded context will exist in isolation. Bounded contexts will need to exchange data with each other. The DDD approach offers a number of approaches for handling the interactions between multiple bounded contexts such as using anti-corruption layers or using [shared kernels](shared_kernel.html).

