# Data Store

## Separate data stores for each MicroService

Do not use the same back-end data stores across microservices. The team should have the freedom to choose the database that best suits the service. Beside that, with a single store it's too easy for microservices written by different teams to share database structures. In this case you end up with the situation where, if one team updates a database structure, other services that use that structure also have to be changed.

Breaking apart the data can make data management more complicated, because the separate storage systems can more easily get out of sync or become inconsistent.