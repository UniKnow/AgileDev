# Shared data microservice Design Pattern.

One of the design principles of microservices is autonomy, meaning that the service is full-stack and has control of all components. However a typical problem, especially when refactoring from an existing monolithic application, is database normalization such that each microservice has the right amount of data. In a transition phase, some applications may benefit from a shared data microservice design pattern.

In this design pattern, some microservices, likely in a [chain](chained.md), may share caching and database stores. This would only make sense if there is a strong coupling between the two services. Some might consider this an anti pattern but business needs might require in some cases to follow this. This would certainly be an anti-pattern for greenfield applications that are designed based on upon microservices.



