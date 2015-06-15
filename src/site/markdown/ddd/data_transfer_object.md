# Data Transfer Object

To have a good encapsulation of the domain entities and related business logic, DDD suggests not to expose the setters of the domain entity to the client. Instead the client can only change the domain entities through business methods defined on the application service/domain entity.

*Therefore:*

* Create a Data Transfer Object (DTO) for those domain entities that are exposed to the client
* Conversion between DTO and domain model will happen in the application layer