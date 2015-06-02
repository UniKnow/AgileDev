# Bounded context

DDD deals with large models by dividing them into different *Bounded Contexts* and being explicit about their interrelationships.

DDD is about designing software based on models of the underlying domain. A model acts as a Ubiquitous Language to help communication between software developers and domain experts. It also acts as the conceptual foundation for the design of the software itself; e.g. how it's broken down into objects or functions.

As you try to model a larger domain, it gets progressively harder to build a single unified model. Different groups of people will use subtly different vocabularies in different part of large organizations.

So instead of creating a unified model of the entire business DDD divides a large system into bounded contexts, which each can have a unified model.

Bounded contexts have both unrelated and shared concepts. Different contexts may have completely different models of common concepts with mechanisms to map between these concepts for integration.

Bounded contexts communicate between each other via their public APIs. This could involve subscribing to events comming from another bounded context, or one bounded context could act like a regular client of another sending commands and queries.