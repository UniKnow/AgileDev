# Cache

Repetitious access to remote resources or global [value objects](ddd/value-objects.md) form a bottleneck for many services. Caching is a technique that can drastically improve the performance. For example by avoiding multiple read operations for the same data.

However there is a price, caching data that the application is accessing will increase the memory usage. Therefore it is very important to obtain a proper balance between the retrieval of the data and the memory usage. The quantity of data being cached and the moment when to load, either in the beginning when the application initializes or whenever it is required for the first time, depends on the requirements of the application.

The cache will identify the buffered resources using unique identifiers. When the resources stored in the cache are no longer required they could be released in order to lower the memory consumption.

Basically there are 2 main caching strategies: Primed and Demand cache.

A cache that is initialized from the beginning with default values is primed cache. A primed cache should be considered whenever it is possible to predict a subset or the entire set of data that the client will request, and to put it to the cache. In case the client request data with a key that matches one of the primed keys having no corresponding data in the cache it is assumed that there is no data in the datasource for that key as well.
Disadvantage of primed cache is that it takes longer for the application to start-up and become functional. There is however a hidden disadvantage and that is that developers assume that the cache will always be populated and that it will always be populated with the entire data set. This assumption could lead to a disaster for your application because:

* the logic to detect cache misses and fetch individual items on a miss never gets written or is written badly.
* Nothing can ever be evicted from the cache.
* Operations has no options to cleanup when faced with memory or data consistency issues in production.

Draw back of not using a primed cache is that when the application is cold and the cache is empty grabbing data from the datastore is inefficient. A solution to bypass the problems as described above is to provide cache miss logic to operate on sets of identifiers beside the single identifier logic. Now, when the system starts, spawn a couple of workers that fetch ids from the resource (in configurable batch sizes) and insert those in the cache. This way you:

* Ensure that code no longer make assumptions about all data being in cache at all times.
* Caches still get filled close to application start, but clients aren't held back by it.
* Cache warming code and normal get-miss-fetch scenarios share the same code.

Demand cache loads information and stores it in cache whenever the information is requested by the system. A demand cache implementation will improve performance while running. A demand cache should be considered whenever populating the cache is either unfeasible of unnecessary.

Object which are applicable for caching are:

* Results of database queries
* XML message
* Results of I/O
* Any other object that is expensive to get.
* Any object that is mostly read.

A cache requires:
* max cache size - Defines how many elements a cache can hold
* eviction policies - Defines what to do when the number of elements in cache exceeds the max cache size. The Least Recently Used (LRU) works best. Policies like MRU, LFU, etc, are usually not applicable in most practical situations and are expensive from performance point of view.
* Time to live - Defines time after that a cache key should be removed from the cache (expired).
* statistics
Professional caches like [EHCache](http://ehcache.org/) or [Guava Cache](https://code.google.com/p/guava-libraries/wiki/CachesExplained) provide this kind of functionality out of the box and are constantly tested.


## See also

* [Distributed cache](http://en.wikipedia.org/wiki/Distributed_cache) - a distributed cache is an extension of the traditional concept of cache used in a single locale. A distributed cache may span multiple servers so that it can grow in size and in transactional capacity. Distributed cache might be interesting in case of demand cache since it will fill up quicker.
* [Spring Cache Abstraction](http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/htmlsingle/#cache)
* [Hibernate 2nd level cache](https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html_single/#performance-cache)
