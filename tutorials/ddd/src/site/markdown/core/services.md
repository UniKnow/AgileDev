# Services.

Some concepts from the domain aren't natural to model as objects. Forcing the required domain functionality to be the responsibility of an `Entity` or `Value Object` either distors the definition of a model based object or adds meaningless artificial objecs.

*Therefore*, when a significant process or transformation in the domain is not a natural responsibility of an `Entity` or `Value Object`, add an operation to the model as a standalone interface declared as a `Service`. Define the interface in terms of the language of the model and make sure the operation name is part of the ubiquitous language. Make the service stateless.