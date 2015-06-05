# Design By Contract

Design by Contract was first explored by Bertrant Meyer. He has invented a language named Eiffel in which contracts are explicitly stated for each method, and checked at each invocation. The contracts define preconditions, postconditions and invariants.

* Preconditions are certain expectations that need to be guaranteed before a client is allowed to perform certain functionality.
* Postconditions are guarantees on exit of certain functionality.
* An invariant is a certain constraint that must be true during the whole lifecycle of the entity.

Design by Contract advocates writing the conditions and invariants first. Constraints can be written by annotations and enforced by a test suite.

The contract will be applied on the method and will normally contain the following pieces of information:

* Acceptable and unacceptable input values/types and their meanings.
* Return values/types and their meaning.
* Exceptions that can occur during execution and their meaning.
* Side effects (for example notifications that are published, etc).
* Preconditions
* Postconditions
* Invariants

In rare cases performance guarantees are also part of the contract.

When applying design by contract a client should not try to verify that the contract conditions are satisfied. Instead the service will throw an exception and the client should be able to cope with that exception.

Design by contract will facilitate code reuse since the contract documents each piece of the code fully; e.g. the contract for a method can be regarded as a form of software documentation for the behavior of that module.

## Related Patterns

* [Defensive Programming](defensive-programming.md) - In case of multi-channel client server or distributed computing the defensive design approach should be taken, meaning that a server component tests (before or while processing a client's request) that all relevant preconditions hold true.
* [Behavior Driven Development](bdd.md) - The enforcing of constraints can be applied by following the Behavior Driven Development methodology.

TODO: http://en.wikipedia.org/wiki/Design_by_contract