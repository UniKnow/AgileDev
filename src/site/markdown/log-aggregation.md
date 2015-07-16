# Log Aggregation

Logs are a critical part of any system, they give you insight into what a system is doing as well what happened. Most processes running on a system generates logs in some form or another and usually these logs are written to files on a local disk. When your system grows to multiple hosts, managing the logs and accessing them can get complicated.  Searching for a particular error across hundreds of logs files on hundreds of servers is difficult without good tools. A common approach to this problem is to setup a centralized logging solution so that multiple logs can be aggregated in a central location.

## Related to

* [Correlation ID](eip/correlation-id) - To track requests there should be a common ID

## See also

* [Log stash](https://www.elastic.co/products/logstash) - Allows to aggregate logs into 1 location.
* [Kibana](https://www.elastic.co/products/kibana) - To search (aggregated) logs.


