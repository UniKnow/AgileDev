# Scale Cube

The scale cube is a 3 dimensional scalability model:

![Scale Cube](images/scale-cube.png)

## X-axis scaling

X-axis scaling consists of running multiple instances of an application behind a load balancer. If there are N instances, then each instance handles 1/N of the load. This form of scaling is a commonly approach of scaling and is usually done by running instances of the application behind a load balancer.
One drawback of this approach is that because each instance potentially accesses all data, caches require more memory to be effective. Another problem with this approach is that it does not tackle the problems of increasing development and application complexity.

## Y-axis scaling

Unlike X and Z axis, which consists of running multiple, identical instances of the application, Y-axis scaling splits the application into multiple, different services. Each service is responsible for 1 or more closely related functions.

## Z-axis scaling

When using Z-axis scaling each server runs an identical copy of the code. In that respect it is similar to X-axis scaling. The big difference is that each server is responsible for only a subset of the data. Some component of the system is responsible for routing each request to the appropriate server. One commonly used routing criteria is an attribute of the request such as the primary key of the entity being accessed.

Z-axis splits are commonly used to scale databases. Data is partitioned (a.k.a sharded) across a set of servers based on an attribute within the record. A router sends each record to the appropriate partition, where it is indexed and stored. A query aggregator sends each query to all of the partitions, and combines the result from each of them.

Z-axis scaling has a number of benefits:

* Each server only deals with a subset of the data.
* Improves cache utilization and reduces memory usage and I/O traffic.
* Improves transaction scalability since requests are typically distributed across multiple servers.
* Improves fault isolation since a failure only makes part of the data inaccessible.

Z-axis scaling also has some drawbacks:

* Increased application complexity.
* Implementing a partition scheme can be tricky, especially if we ever need to repartition the data.
* It doesn't solve the problems of increasing development and application complexity. To solve this we need to apply Y-axis scaling.



