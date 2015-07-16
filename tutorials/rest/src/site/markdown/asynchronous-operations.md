# Asynchronous REST operations

Instead of creating/changing the actual resource, create a temporary one. Instead of returning a `201` (Created) HTTP Resonse, a `202` (Accepted) is returned. This informs the client that the request has been accepted and understood by the server, but the resource is not (yet) created/updated. The temporary resource location will be in the `Location` header. The server can assign a expiry date to all temporary resources and expire them at the appropriate time. When the request is still in progress the changes that were done need to be reverted when expiring. This can be done by invoking cancel (see '[Distributed transactions with REST](distributed-transactions-with-rest)'). This way the server limits for how long (max) any given task can run.

The temporary resource location can store information about the status of the actual resource, an ETA on when the changes will be done, and what is currently being done or processed.

The client requires to query the queued task. When we follow the HATEOAS principle, we can add links to state that we will either cancel, or confirm the queued task.

When the actual resource has been created/updated, the temporary resource can return a `303` (Other). The `Location` header returns the URI to the definitive resource. The server will expire the temporary resource and return a `410` (Gone) later on.

An alternative to this is using the Asynchronous JAX-RS functionality. See '[Asynchronous REST Services with JAX-RS and CompletableFuture](http://allegrotech.io/async-rest.html)' for more details.

## See also
* [Distributed transactions with REST](distributed-transactions-with-rest)
* [Asynchronous operations in REST](https://www.adayinthelifeof.nl/2011/06/02/asynchronous-operations-in-rest/)
* [Making a controller method asynchronous](https://spring.io/blog/2012/05/10/spring-mvc-3-2-preview-making-a-controller-method-asynchronous/)
* [Asynchronous HTTP Request processing with RestEasy](http://docs.jboss.org/resteasy/docs/2.2.1.GA/userguide/html/Asynchronous_HTTP_Request_Processing.html)
* [Example asynchronous Spring Service](http://www.jayway.com/2014/09/09/asynchronous-spring-service/)