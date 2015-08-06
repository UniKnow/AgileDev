#Command Query Responsibility Segregation

CQRS stands for Command Query Responsibility Segregation and helps developers to develop scalable, extensible and maintainable applications. At its hearth the pattern makes a distinction between the model in which information is updated and the model from which information is read. Following the vocabulary of *Command Query Separation* the models are called Command and Query. The rationale behind this separation is that for many problems, particularly in complicated domains, having the same conceptual model for updating information and reading information leads to a complex model that does neither well.

The models may share the same database, in which case the database acts as the communication between the two models, but they may also use separate databases, making the query side database a real-time reporting database. In later case there needs to be some communication mechanism between the two models, or their databases. Commonly this communication is realized by events.

![CQRS](images/cqrs.png)

## Querying

A query returns data and does not alter the state of the model. Once the data has been been retrieved by an actor, that same data may have been changed by another actor, in other words it is stale. If the data we are going to return to actors is stale anyway, is it really necessary to go to the master database and get it from there? Why transform the persisted data into entities if we just want data, not any rule preserving behavior? Why transform those entities to DTOs to transfer them across.

In short, it looks like we are doing a lot of unnecessary work. So why not creating a additional model, (whose data can be a bit out of sync), that matches the DTOs as they are expected by the requester. As data store you can use a regular database but that is not mandatory.

## Commands

A command changes the state of an entity within the command model. A command may be accepted or rejected. An accepted command leads to zero or more events being emitted to incorporate new fact into the system. A rejected command leads to some kind of exception.

One should regard a command as a request to perform a unit of work which is not depending on anything else.

## Domain event

In CQRS, domain events are the source all changes in the query model. When a command is executed, it will change state of one or more entities within the command model. As a result of these changes, one or more events are dispatched. The events are picked up by the event handlers of the query model and those update the query model.

## When to use CQRS.

Like any pattern, CQRS is useful in some places and in others not. Many systems do fit a CRUD model, and should be done in that style. In particular CQRS should only be used on specific portions of a system (Bounded context in DDD lingo) and not to the system as whole; e.g. each bounded context needs its own decision on how it should be modeled.

So far there are the following benefits:

* Handling complexity - a complex domain may be easier to tackle by using CQRS.
* Handling high performance applications - CQRS allows you to separate the load from reads and writes allowing you to scale each independently.

## See also

* [Event Sourcing](event-sourcing) - alternative approach for persisting entities.
