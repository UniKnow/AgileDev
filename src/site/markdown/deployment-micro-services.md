# Deployment of  micro services


So you have applied the different patterns as described within these guidelines and architected your application as a set of services. Each service should be deployed as a set of service instances for throughput and availability.

Forces that (could) influence the decision are:

* Services are written using a variety of languages, frameworks and framework versions.
* Services are independently deployable and scalable.
* Services need to be isolated from each other.
* Need to be able to quickly build and deploy service
* Need to be able to restraint the resources (CPU/Memory) consumed by a service.
* Need to monitor behavior for each service instance.
* Deployment of application must be cost-effective

## Multiple service instances per host

Multiple instances of different services are running on a single host (physical or virtual machine). There are various ways of deploying:

* Deploy each service instance as a JVM process, (for example a Tomcat instance per service instance).
* Deploy multiple service instances in the same JVM, (for example as OSGI bundles)

The resource utilization within this approach is more efficient than the service instance per host. The drawbacks of this approach include:

* Risk of conflicting resource requirements.
* Risk of conflicting dependency versions.
* Difficult to limit the resources consumed by a service instance.
* Difficult to monitor the resource consumption of each service.
* Impossible to isolate each service instance.

## Single service instance per host

Within this approach we deploy each service instance on it's own host. The benefits of this approach include:

* Service instances are isolated from one other.
* There is no possible conflict regarding resource requirements or dependency versions.
* A service instance can consume at most the resources or a single host.
* It is straight forward to monitor, manage, and to redeploy a service instance.

The drawback is less efficient resource utilization compared to multiple service instances per host.

## Service instance per VM

Within this approach we package the service as a virtual machine image and deploy each service instance as a seperate VM.

The benefits of this approach include:

* Straightforward to scale the service by increasing the number of instances.
* VM encapsulates the details of the technology used to build the service.
* Each service instance is isolated.
* VM imposes limits on the CPU and memory consumed by the service instance.

The drawback of this approach include that building a VM image is slow and time consuming.

## Service instance per container

Package the service as a container image ([Docker](https://www.docker.com/)) and deploy each service instance as a container. The benefits of this approach are:

* Straightforward to scale up and down a service by changing the number of container instances.
* Container encapsulates the details of the technology used to build the service. All services are, for example, started and stopped in exactly the same way.
* Each service instance is isolated.
* Container imposes limits on the CPU and memory consumed by a service instance.
* Containers are fast to build and start.

Drawback is that the infrastructure is not as rich as the infrastructure for deploying virtual machines.

TODO

