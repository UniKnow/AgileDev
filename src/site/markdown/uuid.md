# UUID

In an environment with multiple master relational databases, each master must be able to take updates, while also syncing correctly with others. One common concern is that unique identifiers must not conflict acros master databases. Therefore server features such as table level auto-increment can not be used unless each table is only updated on one master database, or each server is given a distinct range of available auto-increment values.

One solution is for each database server or client to generate a UUID (Universal Unique IDentifier) which is a string which is almost guaranteed to never conflict with another UUID generated on a different computer. Therefore each master database, or client connecting to any database, can generate a UUID and use it as the primary key on a table without much concern for replication conflicts.

Another advantage is that they can be freely exposed without disclosing sensitive information, and they are not predictable.

Downside to this choice is that every primary key is then a string which can be disadvantageous to performance on some systems and generating a massive amount of UUIDs can be expensive to process.

## Related Patterns

* [Entities](ddd/entities.md) - IDs of entities should be UUID.