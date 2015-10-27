# Chained microservice Design Pattern.

Chained microservice design pattern produces a single consolidated response to the request. In this case the request from the client is received by Service A, which is then forwarded to Service B, which in its turn may forward it again. All the services are likely using a synchronous HTTP request/response messaging.

The key part to remember is that the client is blocked until the complete chain of request/response is completed. It is important to make the chain not too long. This is because the synchronous nature of the chain will appear like a long wait at the client side, especially if its a web page that is waiting for the response to be shown.

A chain with a single microservice is called singleton chain and may allow the chain to be extended at a later point.