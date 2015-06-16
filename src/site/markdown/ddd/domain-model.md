# Domain Model

Domain Driven Design is based on the idea of solving problems the organisations face through code. This is achieved by focusing on the domain of the business you are working with and the problems they want to solve. This will typically involve rules, processes and existing systems that need to be integrated as part of your solution; e.g. the domain is the ideas, entities and knowledge of the problem you are trying to solve. All of the knowledge around the company and how it operates will form the domain.

The Domain Model is your solution to the problem. The Domain Model usually represents an aspect of reality or something of interest. The Domain Model is often a simplification of the bigger picture, the important aspects of the solution are included while everything else is ignored.

The Domain Model should represent the vocabulary and key concepts of the problem domain and it should identify the relationships among all of the [entities](entities.md) within the scope of the domain. The important this is that the Domain Model should be accessible and understandable by everyone who is involved with the project.

So the Domain is the world of the business and the Domain Model is the structured knowledge of the problem, but why is this important to Domain Driven Design? Well there are 3 reasons:

1. The Domain Model and implementation shape each other - The code that is written should be intimately linked to the Domain Model.Anyone on the team (business, developers, etc) should be able to look at your code and understand how it applies to the problem you are solving. Whenever a decision needs to be made during the course of the project everyone should refer to the Domain Model to look for the answer.
2. The Domain Model is the backbone of the language used by all team members - Every member of the team should use the [ubiquitous language](ubiquitous-language.md). This ensures that technical and non-technical people have a common language so there is no loss of understanding between parties. The ubiquitous language should be directly derived from the Domain Model.
3. The Domain Model is distilled knowledge - The Domain Model is the outcome of an iterative discovering process where everyone on the team is involved to discuss the problem and how it should be solved. The Domain Model should capture how all think about the project and all of the distilled knowledge that has been derived from those collaboration sessions.

## How do you create at a Domain Model?

Creating a Domain Model is an iterative process that attempts to discover the real problem that is faced and the correct solution that is required. It's important to focus a Domain Model on one important problem. Trying to capture the entire scope of a business inside a single Domain Model will be far too overcomplicated and most likely work contradicting as concepts and ideas move around within the organisation, (see [bounded context](bounded_context.md) on how to cope with this).

The problem should be mapped out through talking to business experts to discover the problems they face. This should form the conceptual problem that you are looking to tackle. Business experts won't talk in terms of technical solutions, so during the process important aspects of the problem should be picket out from the language and given concrete definitions to form the ubiquitous language.

There should be a tight feedback loop where everyone on the project discuss the proposed Domain Model to get closer to the solution. This is an iterative approach that will likely encompass code and diagrams to really understand the problem and discover the correct solution.




