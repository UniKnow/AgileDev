# Version Vector

TODO: See http://en.wikipedia.org/wiki/Version_vector.

Version vector is a mechanism for tracking changes to **data** in distributed systems where multiple services might update the data at different times.

Lets assume the scenario of a distributed system where clients can write and read [entities](ddd/entities.md). For these entities we want to track the changes so that successors replace older entities and conflicts are detected and optionally kept for further conflict resolution.

Within the following paragraphs we describe approaches by which we can track, detect and reason about the entities causality.

## Version Vector

Every entity has an associated Version Vector (VV) and sometimes conflicts arise because entities with matching IDs can be updated at the 'same' time and thus have conflicting VVs. VV need identifiers to track whom is responsible for the changes. We can either make the server responsible for changes to a entity (Service Identity - SI), or we can make clients responsible (Client Identifier - CI).

When we track causality by VV with Client Identifiers we will have for each client a entry within the VV; e.g. the number of entries within the VV will match the number of concurrent sources. When a client reads a entity, it will receive the version vector associated with the entity that it reads. When a client submits a new update, the client will receive this vector and the client identity.
With per client entries, we would maintain a counter per client, increment it and replace the version of the client in the version vector for each write.

TODO: Include example of version vector

Drawback of requiring a entry per client in the version vector is that the size of the vector now becomes linear with the number of clients that perform writes.
Another flaw in this approach is the loss of updates when entries are persisted via different services (and persisted in different storage's/data centers). In the figure below we illustrate this problem.


## See also

* [Paper Dotted Version Vectors](https://github.com/ricardobcl/Dotted-Version-Vectors)
* [Dotted Version Vectors](https://github.com/ricardobcl/Dotted-Version-Vectors) - Implementation of the dotted version vectors.