# Exclusive Consumer

If there are multiple message consumer instances consuming from the same queue you will loose the guarantee of processing the messages in order since the messages will be processed concurrently in different threads. Sometimes its important to guarantee the order in which messages are processed.

A common approach in this case is to pin one particular JVM in the cluster to have one consumer on the queue to avoid loosing ordering. The problem with this is that if that particular JVM goes down, no one is processing the queue any more.

With a exclusive consumer we avoid to pin a particular JVM. Instead the message broker will pick a message consumer to get all the messages for that queue to ensure ordering. If that consumer fails, the broker will automatically failover and choose another consumer.

The effect is a heterogeneous cluster where each JVM has the same setup and configuration; the message broker is choosing one consumer to be the master and send all the messages to it until it dies; then you get immediately fail-over to another consumer.

## Parallel exclusive consumer

By including the concept of message groups we can create a kind of parallel exclusive consumers. Rather that all messages going to a single consumer, a message group will ensure that all messages for the same message groups will be sent to the same consumer while that consumer stays alive. As soon as the consumer dies another will be chosen.

When a message is being dispatched to a consumer, the message group is checked. If there is a message group present the broker checks to see if a there is a consumer that owns the message group. If no consumer is associated with a message group, a consumer is chosen. That message consumer will receive all further messages with the same message group until:

* The consumer closes.
* The message group is closed.

## Resources

* [Exclusive Consumer within ApacheMQ](http://activemq.apache.org/exclusive-consumer.html)
* [Parallel exclusive consumers within ApacheMQ](http://activemq.apache.org/message-groups.html)

