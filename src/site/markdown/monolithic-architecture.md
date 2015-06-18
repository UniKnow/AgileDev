# Monolithic Architecture

The application has either a layered of [heaxagonal](hexagonal.html) architecture and consists of different types of components:

* Presentation components - responsible for handling HTTP requests and responding with either HTML or JSON/XML.
* Business logic- the application's business logic
* Database access logic
* Application integration logic - messaging layer

Problem is how we will deploy this application. Forces that influence the solution are:

* There is a team of developers working on the application.
* New team members must become quickly productive.
* The application must be easy to understand and modify.
* The application should be deployed using the continuous integration practice.
* Multiple instances of the application are running to satisfy requirements regarding scalability, and availability.

Most straightforward solution is to build a application with a monolithic architecture, (for example a single java WAR file). This approach has a number of benefits:

* Simple to develop
* Simple to deploy - you simply need to deploy the WAR file on the appropriate runtime.
* Simple to scale - You can scale the application by running multiple copies of the application behind a load balancer.

However once the application becomes large and teams grows in size, this approach has a number of drawbacks:

* The large monolithic code base intimidates developers and the application can be difficult to understand and modify. As a result, development typically slows down.
* The startup of the application will take longer. This will have a impact on developer productivity because of time waisted waiting for the web server to start. This is especially a problem for user interface developers since they usually need to iterative rapidly and redeploy frequently.
* A large monolithic application makes continuous deployment difficult because in case of an update the entire application has to be redeployed.
* A monolithic architecture can only scale in one dimension. This architecture can't scale with an increasing data volume. Each instance of the application will access all of the data, which makes caching less effectively and increases memory consumption and I/O traffic. Also different application components have different resource requirements - one might be CPU intensive while another might be I/O intensive. With a monolithic architecture we cannot scale each component independently.
* A monolithic architecture is also an obstacle for scaling development. Once the application gets to a certain size it is useful to divide the development into several teams that focus on specific functional areas. The trouble with monolithic architecture is that it prevents teams from working independently. The teams must coordinate their development efforts and redeployments.
* A monolithic architecture requires a long term commitment to a technology stack.

## Related Patterns

* [Microservice Architecture](micro-service-architecture) is an alternative to the monolithic architecture.
