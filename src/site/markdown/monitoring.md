# Active Monitoring

Applications must expose runtime information that administrators and operators can use to manage and monitor the system.

The following patterns and guidances are related to monitoring applications:

* [Canary endpoint monitoring](canary-endpoint-monitoring) - Implement functional checks within an application that external tools can access through exposed endpoints at regular intervals and verify whether the dependencies of the service are still operating as expected. This pattern can help to verify that applications and services are performing correctly.
* [Synthetic monitoring](synthetic-monitoring) - constantly checks the application for potential problems by mimicking a user.
* [Log aggregation](log-aggregation) - Each service will create its own log and when an problem occurs you want to dig into them. Finding the problem in multiple logs can become a big pain. Solution for this is to aggregate the logs.
* [Service metering](service-metering) - You may need to meter the use of services in order to plan future requirements, to gain knowledge on how they are used, to bill users, etc.

[Dimensions of Service quality](https://blog.udemy.com/service-quality-management/)

See:
* [reactive microservices monitoring](http://www.mammatustech.com/Home/reactive-microservices-monitoring)
* [Problem 3 of problems micro services](http://eugenedvorkin.com/seven-micro-services-architecture-problems-and-solutions/)
* [Testing strategy in Micro services](http://martinfowler.com/articles/microservice-testing/)
* [Management and Monitoring Patterns and Guidance](https://msdn.microsoft.com/en-us/library/dn600218.aspx)