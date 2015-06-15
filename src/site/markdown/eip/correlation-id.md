# Correlation ID

Micro services call each another and it will be difficult to figure out how one particular request got transformed and which services where called. By using a correlation ID that is passed within the calls between services we enable the tracking of requests and its route.


TODO: determine whether we should use (custom) HTTP Header or URI parameter. Advantage of URI parameter is that it is easier log-able, however caching would become complexer.


## See also

* [Implementing Correlation IDs in Spring Boot](http://java.dzone.com/articles/implementing-correlation-ids-0)