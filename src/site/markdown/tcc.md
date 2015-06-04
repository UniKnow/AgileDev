# Try-Cancel/Confirm.

The Try-Cancel/Confirm pattern focuses on transactions for micro services. A transaction is a set of related interactions (or operations) that may need to be cancelled after they where executed.

When a client initiates a state transition the service will return a handle by which the client can confirm or cancel the state change. If the service does not hear anything after some service specific timeout, it will cancel automatically. Once the workflow has completed successfully the set of returned handles is used to confirm the state transitions. If the service fails, the set of handles that has been collected until the failure is used to cancel the state transitions.

The timeout after which the service will automatically cancel the pending state transitions should be specified by the service.

# Related Patterns

* [API Gateway] - some use cases executed by/via an API gateway require multiple services. The API Gateway will orchestrate the sequence of service instantiations.