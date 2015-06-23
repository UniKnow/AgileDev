# Correlation ID

Microservices call each another and it will be difficult to figure out how one particular request got transformed and which services where called. Another usage of correlation ID is to track state and return address within asynchronous messaging. By using a correlation ID that is passed within the calls between services we enable the tracking of requests and its route, and we can return the response to the proper caller.

As far as the replier is concerned, the correlation ID is an opaque data type and all it has to do is copy the request ID from the request message to the correlation ID in the reply message.

![Correlation ID](images/correlationID.png)

Extra care has to be taken regarding the choice of request ID. The message ID seems a valid choice but in case of intermediaries, the intermediary consumes the original and sends a new message to the replier. During this step the new message might be assigned its own unique message ID. If the replier uses the message ID as the request ID and blindly copies it to the correlation ID, the requester will not be able to correlate the incoming reply message to the original request message.

![Correlation ID with intermediate](images/correlationID-with-intermediate.png)

To overcome this the message should not only contain a message identifier but also a conversation identifier. The requester who initiates the conversation picks a conversation ID while all intermediaries and repliers pass this ID along so that all messages belonging to the conversation carry a common conversation identifier.

![Correlation ID with Conversation ID](images/correlationID-with-conversationID.png)

TODO: determine whether we should use (custom) HTTP Header or URI parameter. Advantage of URI parameter is that it is easier log-able, however caching would become complexer.


## See also

* [Implementing Correlation IDs in Spring Boot](http://java.dzone.com/articles/implementing-correlation-ids-0)