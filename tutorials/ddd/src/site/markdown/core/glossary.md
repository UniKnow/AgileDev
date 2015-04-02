#Glossary

## A

* **Aggregate** - A cluster of associated objects that are treated as a unit for the purpose of data changes. External references are restricted to one member of the `Aggregate`, designated as the root. A set of consistency rules applies within the `Aggregate`'s boundaries.
* **Analysis pattern** - A group of concepts that represents a common construction in business modeling. It may be relevant to only 1 domain or may span many domains (resource fowler 1997, p. 8)
* **Application Service** - Used by external consumers to talk to your system (think Web Services). If consumers need access to CRUD operations, they would be exposed via a application service. Application Services will typically use both Domain Services and Repositories to deal with external requests.
* **Assertion** - A statement of the correct state of a program at some point, independent of how it does it. Typically a `Assertion` specifies the result of an operation or the required input for and operation.

## B

* **Bounded Context** - A division of a larger system that has its own ubiquitous language and domain model. Bounding contexts gives team members a clear and shared understanding of what has to be consistent and what can be developed independently.

## C

* **Client** - A program element that is calling the element under design, using its capabilities.
* **Cohesion** - Logical agreement and dependence.
* **Command** - An operation that causes some change to the system (for example, setting a variable).
* **Context** - The setting in which a word or statement appears that determines its meaning.
* **Context Map** - A representation of the [Bounded Context](#Bounded_Context)s involved in a project and the actual relationships between them and their models.
* **Core Domain** - The distinctive part of the model, central to the user's goals, that differentiates the application and makes it valuable.

## D

* **Data Transfer Object** - A Data Transfer Object (DTO) is a data container which is used to transport data between layers and tiers. It mainly contains of attributes. DTOs are anemic in general and do not contain any business logic.
* **DDD** - See [Domain Driven Design](#Domain_Driven_Design).
* **Declarative Design** - A form of programming in which a precise description of properties actually controls the software.
* **Domain Driven Design** - Domain Driven Design is not a technology or a methodology. DDD provides a structure of practices and terminology for making design decissions that focus and accelerate software projects dealing with complicated domains.
* **Domain Service** - Encapsulates business logic that doesn't naturally fit within a domain object, and are NOT typical CRUD operations, those would belong to a Repository.
* **DTO** - See [Data Transfer Object](#Data_Transfer_Object).

## E

* **Entities** - Entities are characterized by having an identity that's not tied to their attribute values. All attributes in an entity can change and it's still the same entity. Conversely, two entities might be equivalent in all their attributes, but will still be distinct.
* **Event** - An event represents something that took place in the domain. Since an event represents something in the past, it can be considered as a fact and used to take decisions in other parts of the system.

## I

* **Infrastructure Services** - Used to abstract technical concerns (e.g. MSMQ, email provider, etc)

## M

* **Model** - A useful approximation to the problem at hand.
* **Modifier** - See [Command](#Command).

## U

* **Ubiquitous Language** - A language structured around the domain model and used by all team members to connect all activities of the team with the software. The idea is to avoid translations because they blunt communication and makes knowledge crunching anemic.

## V

* **Value Object** - Value objects have no seperate identity; they are defined solely by their attribute values. Though we are typically talking of objects when referring to value types, native types are actually a good example of value types. It is common to make value types immutable.