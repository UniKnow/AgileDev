# System of Record

Multi master database systems that span sites have similar data replicated over multiple locations and the data is updated all the time. Goal of these kind of setups is little or no data loss on failure.

![Multi-Master problem](images/system-of-record-problem.png)

Problem with this solution is that it is impossible to build. The problem is the multi-master replication; updating the same record/table from two or more places on a LAN is already quite difficult, and the problem becomes unmanageable when you combine complex read/write operations, referential integrity, and high latency WAN connections.

Solution for this problem is system of record which states that individual records are updated in a single location only, but may have many copies elsewhere, (both locally as well as on other sites). When clients update particular information they do so on their 'own' master.

## Related Patterns

* [Exclusive consumer](eip/exclusive-consumer.md) - To assure that only 1 instance is handling changes to certain entities we could use the exclusive consumer. In case of microservices this would mean that all instances of a particular service are listening to a particular queue and that the commands which are put on the queue are grouped by (for example) the ID of the [entity](ddd/entities.md) at which the command applies. This would mean that the part that puts the command on the queue needs to know how to group the commands, (getting ID of entity at which the command applies from command). In the figure below we attempt to show how such a setup would look. Within this example there are 2 instances of the service whom both receive requests via their `Request Handler`. The `Request Handler` transforms the incoming requests into Commands, determine the group at which they apply, and put them on the queue. The `Command Handler` will listen on the queue for commands within certain groups (standard functionality ActiveMQ) and process the commands within that group in the order that they where posted. Since there is only 1 command handler listening for a certain group we assure the system of record.

![System of Record & Exclusive Consumer](images/system-of-record-and-exclusive-consumer.png)
