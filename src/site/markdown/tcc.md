# Try-Cancel/Confirm.

The Try-Cancel/Confirm pattern focuses on transactions for microservices. A transaction is a set of related interactions (or operations) that may need to be cancelled after they where executed.

When a client initiates a state transition the service will return a handle by which the client can confirm or cancel the state change. If the service does not hear anything after some service specific timeout, it will cancel automatically. Once the workflow has completed successfully the set of returned handles is used to confirm the state transitions. If the service fails, the set of handles that has been collected until the failure is used to cancel the state transitions.

The timeout after which the service will automatically cancel the pending state transitions should be specified by the service.

## Using RESTAT

You will need to deploy the coordinator as a war archive. The archive is contained in the `bin` folder of the [narayana download](http://www.jboss.org/jbosstm/downloads/5.2.0.Final/binary/narayana-full-5.2.0.Final-bin.zip) (restat-web.war).

[RESTful transactions Quickstarts](https://github.com/jbosstm/quickstart/tree/master/rts/at)
[raw-xts-api-demo](https://github.com/jbosstm/quickstart/tree/master/XTS/raw-xts-api-demo)
[compensating transactions](https://github.com/jbosstm/quickstart/tree/master/compensating-transactions)

# Related Patterns

* [API Gateway] - some use cases executed by/via an API gateway require multiple services. The API Gateway will orchestrate the sequence of service instantiations.

