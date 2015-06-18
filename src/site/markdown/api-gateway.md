# API Gateway

Your system must expose to clients functionality that they can use. However the granularity of the APIs provided by the microservices is often different from what the client needs. Microservice APIs typically provide fine-grained APIs, which means that clients need to interact with multiple services. Also the number of service instances and their locations (hoss, port, ...) changes dynamically, and the partitioning of the services can change over time. All this should be hidden from the clients.

To solve this we expose an API gateway that is the single entry point for all clients.

![API Gateway](images/api-gateway.png)

The API gateway handles the requests in the following 2 ways:

1. Requests are simply proxied/routed to the appropriate service.
2. Requests are fanning out to multiple services

Rather than exposing a one size fits all style API, the API gateway can expose a different API for each client. The API Gateway might also verify that the client is authorized to perform the request.

# Related Patterns

* [Service Connector](service-connector.md) - Provide high level interface that hides implementation details regarding communication, thereby making the use of the microservice easier.
* [Try-Cancel/Commit](tcc.md) - By this pattern we can realize distributed transactions for microservices.
