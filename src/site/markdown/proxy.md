# Proxy microservice Design Pattern.

Proxy microservice design pattern is a variation of [Aggregator](aggregator.md). In this case, no aggregation needs to happen on the client but a different microservice may be invoked based upon the business need. You may like to use this pattern when you don't want to expose each service to the consumer but instead have them all go through an interface.

The proxy may be a dumb proxy in which case it just delegates the request to one of the services. Alternatively, it may be a smart proxy where some data transformation is applied before the response is served to the client.

Just like Aggregator, Proxy can scale independently on [X-axis](scale-cube.md) and [Z-axis](scale-cube.md) as well.
