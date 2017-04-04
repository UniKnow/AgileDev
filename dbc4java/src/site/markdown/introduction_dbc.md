# Introduction Design By Contract.

The principle of *design by contract* was first introduced by Bertrant Meyer and the basic idea is modules in software systems should be designed to follow contracts. The contracts shall specify under which circumstances a method can be used. Most methods request some parameter(s) to be sent to the method when it is invoked. Sometimes it is necessary that the parameters have certain values for the method to be able to execute and produce correct output. The contract is all about specifying acceptable values for the parameters both before and after the execution of the method.

Contracts have suppliers and consumers. The supplier can be a method or a class that already is written and provides some functionality to other developers. The supplier code will be called from other parts of the system when it is needed. The code that calls the supplier will be the client.

In order to verify that the contract is followed by both the supplier and the client, constraints will be inserted during the design of the contract. An example of an constraint would be that a certain integer should have a positive value. Constraints are basically boolean expressions.

## Preconditions and Postconditions.

A method contract is a set of constraints that are evaluated before and after the execution of one method. The precondition that is spoken of are the constraints that must be fulfilled before the code in a method is executed. A postcondition is a guaranteed state or value that the result must satisfy when the code has been executed. The pre- and postconditions function as a guarantee for both the client and supplier.

A simple contract is shown below. The client of the method must send a parameter of type `int` that is smaller than `MAX_VALUES`. In return the client can be certain that that the method returns the value placed at the requested postion in the array.

    @Named
    @Validated
    public class SimpleMethodContract {

        /**
         * Max number of values that can be persisted.
         */
        public static final int MAX_VALUES = 10;

        private int values[] = new int[MAX_VALUES];

        public int getValue(@Max(MAX_VALUES - 1) @Min(0) final int index) {
            return values[index];
        }

    }

If a method has a precondition which say that a `index` must be within a certain range (`0` .. `MAX_INDEX`), then every programmer must ensure that the method is invoked with a `index` which is within the range. That is the obligation of the client. A consequence of the constraint is that the method may start executing its code without verifying that the `index` is within the range (this is done by the framework), which is a clear benefit. In case of a postcondition the client knows that the method returns a valid value (verified by framework) and that the instance is in a state according to the postconditions; e.g. there is no need for the client to verify the output.

As a summary you can say that if the client invoke a method with a satisfied precondition then the method promise to deliver an output that is satisfied according to the postcondition. On the other hand, if the postcondition is not satisfied, a `ValidationException` is raised at the client and it is there that the error must be fixed, not in the supplier. A `ValidationException` during postcondition means a fault in the supplier and it must be fixed in the invoked method. The constraints help the developer to find the error and to fix it in the right space.



