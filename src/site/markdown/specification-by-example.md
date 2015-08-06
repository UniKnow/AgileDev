# Specification by Example

Specification by Example is a collaborative approach to defining requirements and business oriented functional tests for systems based on capturing and illustrating requirements using realistic examples instead of abstract statements. This approach is particularly successful for managing requirements and functional tests on large scale projects. The majority of the information captured in traditional specification documents, (such as requirements specification) can be captured as executable specifications in teh form of tests. When a [Behaviour Driven Development](bdd.md) approach is taken you effectively write detailed specifications.

Natural language is ambiguous and context dependent. Requirements written in such language alone can't provide a complete picture for development or testing. Developers and testers have to interpret requirements to produce software and test scripts which might lead to different interpretations. Small differences in understanding have a cumulative effect, often leading to problems that require rework after delivery.

Instead of waiting for specifications to expressed precisely for the first time, teams illustrate specifications using examples. The team works with the business users to identify key examples that describe the expected functionality. During this process, developers and testers often suggest additional examples that illustrate edge cases. This process flushes out functional gaps and inconsistencies and ensures that everyone involved has a shared understanding of what needs to be delivered.

If the system works correctly for all the key examples, then it meets the specification that everyone agreed on. Key examples effectively define what the software needs to do. They are both the target for development and an objective evaluation criterion to check to see whether the development is done.

If the key examples are easy to understand and communicate, they can be effectively used as unambiguous and detailed requirements.

Specification by Example is also known as example driven development, executable requirements, acceptance test-drive development (ATTD) or Agile Acceptance Testing.

**Note:** Specification by example only works in a environment where both sides are collaborating and not fighting.

## See also

* [Behavior Driven Development](bdd.md) - Way to describe detailed specifications.
* [Domain Driven Design](ddd/introduction-ddd.md) - Provides a ubiquitous language that should be used when writing down the examples.
* [Cucumber]() - Framework for specifying tests.

See:

* [Specification by Example](https://en.wikipedia.org/wiki/Specification_by_example)
* [book specification by example](http://specificationbyexample.com/key_ideas.html)
* [Marting Fowler specification by example](http://martinfowler.com/bliki/SpecificationByExample.html)
* [Writing executable specifications](http://www.infoq.com/presentations/Executable-Specifications)

