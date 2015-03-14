#Side effect free functions.

Interactions of multiple rules or compositions of calculations become extremely difficult to predict. The developer calling an operation must understand its implementation and the implementation of all its delegations in order to anticipate the result. The usefulness of any abstraction of interfaces is limited if the developers are forced to pierce the veil.

*Therefore*, place as much as possible of the program into functions/operations that return results with no observable side effects. Strictly segregate method which result in modifications to observable state into very simple operations that do not return domain information. Further control side effects by moving complex logic into `Value Objects` when a concept fitting the responsibility present itself.