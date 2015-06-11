# Circuit Breaker

One of the properties of a remote call is that it can fail, or hang without a response until some timeout limit is reached. If there are many callers of this unresponsive service this can result in the caller run out of critical resources leading to cascading failures across multiple systems. To prevent this kind of catastrophic cascade we can use a circuit breaker.

The basic idea behind the circuit breaker is that you wrap external calls in a circuit breaker object. The circuit breaker object monitors for failures and once the failures reach a certain threshold, the circuit breaker trips, and all further calls via the circuit breaker return an error without the call being made at all. Usually there is also some kind of monitoring whether the circuit breaker has tripped.

This simple form of circuit breaker would need an external intervention to reset it when things are well again. An improvement would be to have the breaker itself detect if the underlying cells are working again.

## Related patterns

* [Active Monitoring](active-monitoring.md) - Circuit breakers are a valuable place for monitoring and any change in the breaker state should be logged. Breaker behavior is often a good source of warnings about deeper troubles in the environment.
* [API Gateway](api-gateway.md) - An API gateway orchestrates the calls between the diverse number of micro services. Each of these calls should be via a orchestrator.

## See also

* [Hystrix](https://github.com/Netflix/Hystrix) -  A sophisticated tool for dealing with latency and fault tolerance for distributed systems. It includes an implementation of the circuit breaker pattern
* [JRugged](https://github.com/Comcast/jrugged) - A Java library of robustness design patterns.

