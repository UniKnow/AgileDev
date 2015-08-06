# Event Sourcing

Event sourcing ensures that all changes to application state are stored as a sequence of events. We can not only query these events, we can also use the event log to reconstruct past states, and as foundation to 'automatically' adjust the state to cope with retroactive changes.

The fundamental idea of event sourcing is that of ensuring every change to the state of an application is captured in an event, and that these events are themselves stored in the sequence they where applied. With these events we can:

* Completely rebuild: We can discard the application state completely and rebuild it by re-running the events from the event log on an empty application.
* Temporal query: We can determine the application state at any point in time. Usually we do this by startign with a blank state and rerunning the events up to a particular time or event. We can take this furhter by considering multiple time lines.
* Event replay: If we find a past event was incorrect, we can compute the consequences by reversing it and later events and then replaying the new events and later events. The same technique can handle events received in the wrong sequence - a common problem with systems that communicate with asynchronous messaging.

## Storing application state
The simplest way to use event sourcing is to start from a blank application state and then applying the events to reach te desired state. This could however be a slow process, particularly if there are many events.
A faster alternative is to hold the current application state within memory. Should the application crash it replays the stored events. To even further speedup the process one could take at regular intervals snapshots and only replay the events that occurred after the snapshot was taken. New snapshots can be made at any time in parallel without bringing down the running application.

## See Also

* [Query model of CQRS](cqrs.md) - Holds current application state.
* [Retroactive Event](retroactive-event.md) - Approach to revert faulty events.
* [Snapshot][snapshot.md]