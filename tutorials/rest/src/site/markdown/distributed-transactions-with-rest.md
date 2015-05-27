# Distributed transactions with REST

With the rise of micro services the discussion around related technologies and protocols such as security, transactions and reliability are also becoming more relevant. This article focus on a simple solution which fits the following design constraints:

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

TODO







