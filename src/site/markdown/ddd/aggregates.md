# Aggregates.

It is difficult to guarantee the consistency of changes to objects in a model with complex associations. Invariants need to be maintained that apply to closely related groups of objects, not just discrete objects. Yet cautious locking schemes cause multiple users to interfere pointlessly with each other and make a system unusable.

*Therefore*, cluster the `Entities` and `Value Objects` into `Aggregates` and define boundaries around each. Choose one `Entity` to be the root of each `Aggregate`, and control all access to the object inside the boundary through the root. Allow external objects to hold references to the root only.
Transient references to internal members can be passed out for use within a single operation only.
Because the root controls access, it cannot be blindsided by changes to the internals. This arrangement makes it practical to enforce all invariants for objects in the `Aggregate` and for the `Aggregate` as a whole in any state change.