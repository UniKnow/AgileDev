# Unit Testing

Unit testing exercises the smallest piece of testable software in the application to determine whether it behaves as expected. In this context, a 'unit' is often a function or a method of a class instance. Often, difficulty in writing a unit test can highlight when a module should be broken down into independent more coherent pieces and tested individually. Thus alongside being a useful testing strategy, unit testing is also a powerful design tool, especially when combined with [behavior driven development](bdd.md).

Within unit testing we can make a distinction based on whether or not the unit under test is isolated from its collaborators:

* Sociable unit testing - focuses on testing the behaviour of units by observing changes in their state. This treats the unit under test as a black box tested entirely through its interface.
* Solarity unit testing - looks at the interactions and collaborations between an object and its dependencies, which are replaced by [mocks](mocks.md).

These styles are not competing and are frequently used in the same codebase to solve different testing problems.

The domain logic within a microservice often manifests as complex complex calculations and a collection of state changes. Since this kind of logic is highly state based there is often little value in trying to isolate the units. This means that we should apply solarity unit testing to the [domain model](ddd/domain-model.md).

With plumbing code like [repositories](ddd/repositories.md), and [service connectors](service-connector.md) the  purpose of the unit tests is to verify the logic used to produce requests or map responses from external dependencies. As such using [mocks](mocks.md) provides a way to control the request-response cycle in a reliable and repeatable manner. Advantage of testing these components at this level is that they provide faster feedback than integration tests and we can force error conditions by having the mocks replicate error conditions.

Coordination logic such as [services](ddd/services.md) care more about the messages passed between the modules and the domain than any complex logic. Using mocks allows the changes to the domain to be verified. If a service requires too many mocks, it is usually a good indicator that some concepts should be extracted and tested in isolation.

## See also

* [ContiPerf] - A Java library for measuring performance by running JUnit tests. This could provide a early hint regarding performance bottle necks.