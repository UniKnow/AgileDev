# FAQ

## What is Domain Driven Design?

It is a development approach that deeply values the domain model and connects it to the implementation.

## What is a ubiquitous language?

A set of terms used by all people involved in the domain, domain model, implementation and backends. The idea is to avoid translation because that blunts communication and makes knowledge crunching anemic. Investing in ubiquitos language pays off in that it makes communication clearer, and allows teams to see more opportunities.

## What is a bounded context?

A division of a larger system that has its own ubiquitous language and domain model. As with other As with other DDD concepts bounded contexts are most valuable when carried through into the implementation.

## How can I communicate between bounded contexts?

Exclusively in terms of their public API. This could involve subscribing to events coming from another bounded context and/or one bounded context could act like a regular client of another bounded context sending command and queries.

## What is an event?

An event represents something that took place in the domain. They are always named with a past particle verb, such as `DeviceRegistered`. It's not unusual, but not required, for an event to name an aggregate or entity to which it is related to.
Since an event represents something in the past, it can be considered as statement of fact and used to take decisions in other parts of the system.

## What is an command?

Changes to the domain are requested by sending commands. They are named with a verb in the imperative mood and may include the aggregate/entity to which it is related, for example `RegisterDevice`. Unlike a event, a command is not a statement of fact; it's only a request, and thus may be refused (a typical way to convey refusal is to throw an exception).

## Why are commands and events immutable?

For the purpose of this question, immutability is not having any setters or other methods which change the internal state.
Commands are immutable because they are sent directly to the domain model side for processing. They do need to change during their projected lifetime from client to server.
Events are immutable because they represent domain actions that took place in the past.

## What is CQRS?

CQRS means "Command Query Responsibility Segregation" and like the name states the responsibility between commands (write requests) and queries (read requests) is segregated; e.g the write and read requests are handled by different objects.
We can further split up the data storage, having separate write/read stores. Though separating read/write stores is often discussed in relation with CQRS, this is not CQRS itself. CQRS is just the first split of commands and queries.

## Should I use push or pull when publishing events?

Push has the advantage that events can be published as they happen. Pull has the advantage that read sides can be more active and independent.