# Aggregator Microservice Design Pattern

In its simplest form an Aggregator would be a simple web page that invokes multiple services to achieve the functionality required by the application. Since each service is exposed using a lightweight interface (for example REST), the web page can retrieve the data and process/display it accordingly.

Another option for Aggregator is where not visualization is required, and instead it is just a higher level composite microservice which can be consumed by other services. In this case, the aggregator would just collect the data from each of the individual microservice, apply business logic to it, and further publish it as a REST endpoint. This can then be consumed by other services that need it.

This design pattern follows the [DRY](dry.md) principle. If there are multiple services that need to access multiple services, then it is recommended to abstract that logic into a composite microservice and aggregate that logic into one service. An advantage of abstracting at this level is that the individual services can evolve independently and the business need is still provided by the composite microservice.

Note that each individual microservice has its own (optional) caching and database. If the Aggregator is a composite microservice, then it may have its own caching and database layer as well.

An Aggregator can scale independently on [X-axis](scale-cube.md) and [Z-axis](scale-cube.md) as well.

