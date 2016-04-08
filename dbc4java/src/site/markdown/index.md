# What is dcb4java

 `dbc4java` is a pragmatic and extensible general purpose validation framework for any kind of java objects and allows you:

 * to easily validate objects on demand
 * to specify constraints for class fields and methods.
     * specifying constraints for method parameters that are automatically checked when a method is called (pre-conditions).
     * requiring a certain object state before a method is called (pre-conditions).
     * enforcing object validation after an object has been created (invariants).
     * enforcing object validation after a method on an object has been called (invariants).
     * specifying constraints for a method's return value (post-conditions).
 * to configure constraints via annotations.

 Within [introduction design by contract](introduction_dbc.html) we provide a deeper understanding of design by contract. The [Usage dbc4spring](usage.html) chapter describes how the `dbc4java` framework can be used within your project. The [Validation Constraints](validation_constraints.html) provides an overview of the constraint annotations that are default supported by `dbc4java`.

*NOTE:* The `dbc4java` framework doesn't support constraints on java constructors or static methods!
