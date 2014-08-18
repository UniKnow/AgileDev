# Behavior Driven Development

At its core, behavior-driven development (BDD) is a specialized version of Test Driven Development (TDD) which focuses on behavioral specification of software units.

Test-driven development is a software development methodology which essentially states that for each unit of software, a software developer must:
 
1. define a test set for the unit first;
2. then implement the unit;
3. finally verify that the implementation of the unit makes the tests succeed.
 
This definition is rather non-specific in that it allows tests in terms of high-level software requirements, low-level technical details or anything in between. Due to this lack of clarity BDD was developed which specifies that tests of any unit of software should be specified in terms of the *desired behavior* of the unit. Borrowing from agile software development the "desired behavior" in this case consists of the requirements set by the business — that is, the desired behavior that has business value for whatever entity commissioned the software unit under construction.

Following this fundamental choice, a second choice made by BDD relates to how the desired behavior should be specified. In this area BDD chooses to use a semi-formal format for behavioral specification which is borrowed from user story specifications. Each user story should, in some way, follow the following structure:

Title: The story should have a clear, explicit title.

Narrative
A short, introductory section that specifies

* who (which business or project role) is the driver or primary stakeholder of the story (the actor who derives business benefit from the story)
* which effect the stakeholder wants the story to have
* what business value the stakeholder will derive from this effect

Acceptance criteria or scenarios

* a description of each specific case of the narrative. Such a scenario has the following structure:
* It starts by specifying the initial condition that is assumed to be true at the beginning of the scenario. This may consist of a single clause, or several.
* It then states which event triggers the start of the scenario.

Finally, it states the expected outcome, in one or more clauses.

BDD suggested the following template for a textual format which has found wide following in different BDD software tools. A very brief example of this format might look like this:

    Story: Returns go to stock
    
    As a store owner
    I want to add items back to stock when they're returned
    So that I'm able to keep track of stock
    
    Scenario 1: Refunded items should be returned to stock
    Given a customer previously bought a black sweater from me
    And I currently have three black sweaters left in stock
    When he returns the sweater for a refund
    Then I should have four black sweaters in stock
    
    Scenario 2: Replaced items should be returned to stock
    Given that a customer buys a blue garment
    And I have two blue garments in stock
    And three black garments in stock.
    When he returns the garment for a replacement in black,
    Then I should have three blue garments in stock
    And two black garments in stock
    
