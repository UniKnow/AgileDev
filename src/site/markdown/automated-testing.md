# Automated testing

**TODO**

![Testing pyramid](images/testing-pyramid.png)

The testing pyramid essentially points out that you should have many more low level unit tests than high level end-to-end tests running. The layers we can distinct are:

* Exploratory -
* End-to-End
* Integration - By integration tests the interaction between two or more services is explicitly tested.
* Component -
* Unit - In this context, a 'unit' is often a function or a method of a class instance. See [unit testing](unit-testing.md) for more details.

## Test Data

One of the harder challenges within automated testing is generating valuable test data. Hard coding assumptions about data availability can be a fragile approach as there are no guarantees that the data continue to exist. Furthermore, some tests may require data consistency across multiple services.

One of the robust strategies is to create the test data during the test run, as you guarantee the data exists before using it. This requires however that each service allows creating new resources, which isn't always the case. A solution for this could be to expose in the test environments test-only endpoints to facilitate test data creation. These end-points are of course not exposed in the production environment.

An alternative strategy is to have each service publish a cohesive set of test data that is guaranteed to be stable.

## See also

* [Consumer-based testing] - To verify the integration between multiple services we could rely on consumer based testing.