# Distributed transactions with REST

With the rise of microservices the discussion around related technologies and protocols such as security, transactions and reliability are also becoming more relevant. This article focus on a simple solution which fits the following design constraints:

1. Using a lightweight transaction model.
2. Avoiding extensions to the HTTP protocol to maximize adoption.
3. Deploying the transaction related functionality as a RESTful service.
4. Keeping the participants unaware that they are part of a transaction.

The solution will make it possible to group multiple REST services as a single step, as well as to guarantee that the set of resources that belong to multiple REST services remain consistent.

## The problem

The figure below shows an environment in which Devices and Products that the device is authorized to use are in different systems. When a device is deleted the authorizations for the products that where in use by the device need to be cancelled also so that no royalties have to be paid for those. To keep the system consistent both the deletion of the device and cancellation of the authorizations need to be successful, otherwise the authorizations system will contain references to a non existing system.

![Problem Illustration](images/distributed-transaction-problem-illustration.jpg)

Note that the crucial point here is that when the cancellation of the authorizations is failing the device has already been deleted.

## Assumptions

The solution we want to create is based on the following assumptions:

* A business Transaction is sending Requests to different REST services. Each Participant is autonomous and loosely coupled with the Transaction and the other participants. For simplicity we assume there is no ordering among the Requests.
* The Transaction needs to be atomic; e.g. the Transaction needs to either happen entirely (all Requests succeed) or not at all. In the case of failure the end result needs to be as if none of the Requests happened in the first phase.
* Participants reserves resources on behalf of the Requests in the Transaction.
* Each Request done within a Transaction may need to be cancelled after it has been executed. Consequently each Request needs to have a cancellation associated with it.
* Each Participant will await a confirmation for every Request.

## Solution overview

The previous assumptions lead to the following solution;

A client starts interacting with multiple REST services. Each interaction that results into a state transition of a resource results in a URI that later can be used to confirm or cancel it. If the service doesn't hear anything after some service specific timeout, it will cancel the change automatically.
Once the client successfully completes, the returned URIs will be used to confirm the state transitions of the resources.
If the workflow fails the set of URIs that have been collected until the failure occurred, will be used to signal each of the REST services with uncommitted changes to cancel their state transitions.

### Cancel vs Confirm

One of the guarantees of classic transactions is that every change is temporary (subject to rollback) until the application explicitly indicates that everything is done and can be saved (committed). For REST the same should be possible, however there is no classical rollback. Due to that the notion of rollback is replaced by cancel and likewise the notion of commit is replaced by confirm.

The goal of cancel is to revert changes across multiple participants in case of failures within a transaction. The cancellation mechanism will be internal and unconditional within each REST service. This way, each REST service will eventually cancel after some time-out.

The goal of confirm is to let the REST service know that the workflow has been ended successfully. If by default everything will be cancelled there needs to be a way to perform otherwise. Within this solution that will be done via an explicit confirm request to the REST service(s) involved. In order to do this with REST the minimal requirement is a URI providing the functionality to confirm the resource state changes. Only the REST service itself should determine what that URI is. Within the response there should be an indication on when the URI will expire, indicating when the REST service itself will cancel automatically.

It is up to the Client and REST service to negotiate an appropriate timeout duration.

Confirmation requests that come after the URI has timeout and cancelled on its own should result in a `404`.

### Transaction Coordinator

To prevent that each application developer has to implement its own distributed transaction logic there should be a transaction coordinator that will take care of recovery and failure handling. This component can be delivered as a (REST) service.

The URIs which are returned by the REST service will be handed over to the coordinator. The coordinator will delegate the confirmations to all participants. If all goes fine the response would be `204`.

When the coordinator fails to enforce the confirmation there needs to be a way to communicate the problem back to the application. If every participant in the transaction timed out (and cancelled) this is indicated by a `404` ("Not Found") error. Everything else will result in a `409` ("Conflict") status code. The response body could contain a detailed log, showing which of the given URIs could be confirmed and which could not be confirmed.

Recovery is needed in two typically cases:

* The coordinator crashed - once it comes back up, it retries the remaining URIs for which it was confirming the transaction.
* Any participant crashed or becomes unreachable due to network errors. In this case the coordinator simply retries confirmation requests.

## See also:

* [spring-tcc-transaction](https://github.com/UniKnow/AgileDev/tree/develop/spring-tcc-transactions) - Spring implementation of Try Cancel/Confirm methodology.
* [CQRS Event Sourcing](https://github.com/UniKnow/AgileDev/tree/develop/tutorials/cqrs-eventsourcing) - Example of CQRS event sourcing including Try Cancel/Confirm methodology.





