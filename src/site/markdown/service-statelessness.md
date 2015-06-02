# Service Statelessness

The management of excessive state information can compromise the availability of a service and undermine its scalability principle. The services within a distributed application are deployed among multiple resources to benefit the scaling out functionality. The most significant factor complicating addition and removal of service instances is the internal state maintained by the service. In case of failure, this information might even be lost.

Services are therefore ideally designed to be stateless. Instead their state and configuration is stored externally in [storage offerings](storage-offerings.md) or provided by the component with each request.