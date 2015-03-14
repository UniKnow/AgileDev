# Entities

*Many object are not fundamentally defined by their attributes, but rather by a thread of continuity and identity.*

Some objects are not defined primarily by their attributes. They represent a thread of identity that runs through time and often across distinct representations. Sometimes such an object must be matched with another object even though attributes differ. Mistaken identity can lead to data corruption.

*Therefore*, when an object is distinguished by its identity, rather than its attributes, make this primary to its definition in the model. Keep the class definition simple and focused on life cycle continuity and identity. Define a means of distinguishing each object regardless of its form or history. Be alert to requirements that call for matching objects by attributes. Define an operation that is guarenteed to produce a unique identity for each object. The model must define what it *means* to be the same thing.