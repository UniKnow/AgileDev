# Behavior Driven Development

Behavior Driven Development is a software development process that emerged from [Test Driven Development](http://en.wikipedia.org/wiki/Test-driven_development). Behavior Driven Development combines the general techniques and principles of TDD with ideas from [Domain Driven Design](ddd/introduction_ddd.md). TDD states that for each unit of software (class in case of OO), a software developer must:

1. Define a test set for the unit *first*.
2. Then implement the unit.
3. Verify that the implementation of the unit makes the tests succeed.

Extension to this is that within BDD the tests should be defined in terms that has business value; e.g. the tests should be written in a form that is using business terminology (defined within the [ubiquitous language](ddd/introduction_ddd.md)).

On top of that BDD defines how the desired behavior should be specified. Business analysts and developers should work together and specify the desired behavior in the form of user stories that have the following structure:

    Title: Explicit and clear title

    Narrative: Short introduction that specifies
        * Who is the driver or primary stakeholder of the story, (business actor).
        * What effect the story would have.
        * What benefits the stakeholder will derive from this effect.

    Scenarios: Description of each specific case according to the following structure

        * Given: Specifies the initial situation. This may consist of
                 single clause, or several (combined by AND).
        * When : States which event triggers the start of the scenario.
        * Then : States the expected outcome in one or more clauses.

## Related Patterns

* [Test Driven Development](tdd.md) - Behavior Driven Development emerged from Test Driven Development.
* [Domain Driven Design](ddd/introduction_ddd.md) - Behavior Driven Development borrows the concept of the ubiquitous language from DDD to define the tests in business terminology.

## See also

* [Cucumber](https://cucumber.io/) - BDD testing framework for several languages, (Ruby, Java, ...).
* [An Introduction to BDD Test Automation with Serenity and JUnit](https://www.voxxed.com/blog/2014/12/introduction-bdd-test-automation-serenity-junit/)