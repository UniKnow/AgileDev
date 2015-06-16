# Modeling the Domain

## Context

The Domain Model should represent the vocabulary and key concepts of the problem domain and it should identify the relationships among all of the entities within the scope of the domain.

As you try to model a larger domain, it gets progressively harder to build a single unified model. Different groups of people will use subtly different vocabularies in different part of large organizations.

So instead of creating a unified model of the entire business we should divides a large system into bounded contexts, which each can have a unified model.

## Solution

In ArchiMate the grouping relationship is suited to group domains.

![Example Domains](images/example-domains.png)

Within the example above we distinct 2 domains, Claim handling and Accounting, each having its own business processes and objects.



