# Factories

*When creation of an object, or an entire `Aggregate`, becomes complicated or reveals too much of the internal structure `Factories` provide encapsulation.*

Creation of an object can be a major operation in itself, but  complex assembly operations do not fit the responsibility of the created object. Combining such responsibilities can produce ungainly designs that are hard to understand. Making the client direct construction muddies the design of the client, breaches encapsulation of the assembled object or `Aggregate`, and overly couples the client to the implementation of the created object.

 *Therefore*, shift the responsibility for creating instances of complex objects and `Aggregates` to a separate object, which may itself have no responsibility in the domain model but is still part of the domain design. Provide an interface that encapsulates all complex assembly and that does not require the client to reference the concrete classes of the object being instantiated. Create entire `Aggregates` as a piece, enforcing their invariants.