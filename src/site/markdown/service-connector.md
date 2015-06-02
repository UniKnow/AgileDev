# Service Connector

Clients must know a lot about services in order to use them. REST APIs require clients to use specific media types, HTTP Headers, and specific request methods. Some web services require data to be encrypted or demand that clients submit authentication tokens. The client must also know the service's address, how to serialize requests, and parse responses. Each client could develop a custom solution to meet the specific requirements of the service but that would often result in duplicate code.

Instead we should create a service connector that encapsulates the logic a client can use to call a certain service.

![Service Connector](images/service-connector.png)

Service connectors make services easier to use by hiding the communication specifics. The service connector encapsulate the generic communication logic required to use the service and also include the logic that is specific go the given service. Service connectors are typically responsible for service discovery and connection management, request dispatch, response handling, and some error handling
