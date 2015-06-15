# Exception Handling

TODO: Write nice intro.

## Exception handling and logging

...

## Best practices

* Use checked exceptions for recoverable errors and unchecked exceptions for programming errors - Checked exceptions ensure that you handle certain error conditions, but at the same time it also adds a lot of clutter in the code and might make it unreadable. Also it is only reasonable to catch exceptions if you have alternatives or recovery strategies.
* Avoid unnecessary exception handling - Exceptions are costly and can slow down your code. Don't just throw and catch exceptions, if you can use standard return values to indicate result of an operation. Also avoid unnecessary handling by fixing the root cause.
* Never swallow the exception in catch block.
* Declare specific checked exceptions that method can throw - Declare specific checked exceptions that can be thrown by your method. If there are too much checked exceptions, you should wrap them in your own exception and add information in the exception message.
* Never catch the `Exception` class - The problem with catching `Exception` is that if the method that you are calling adds a new checked exception to its method signature you will never know about it and the fact that your code now might be wrong and might break at any point in time.
* Never catch `Throwable` class - JVM errors are also subclass of `Throwable` and these errors are irreversible conditions that cannot be handled by the JVM itself. The JVM might even not invoke your `catch` clause on an error.
* Always wrap exceptions correctly - the original exception should always be included within the exception that wraps it.
* Either log the exception or throw it but never do both - Logging and throwing will result in multiple messages in the log for a single problem in the code.
* Never throw any exception form finally block - if we would throw an exception from the finally block we might hide the original first exception and correct reason would be lost forever.
* Only catch exception you can handle - Catch an exception only if you want to handle it or to provide additional contextual information in that exception.
* Don't print stack traces to the console.
* Use finally blocks instead of catch blocks if you are not going to handle exception. If a method throws some exception which you do not want to handle, but still want to perform some cleanup, then do this cleanup in the `finally` block. Do not use `catch` block.
* Prefer exceptions to return codes - it is preferable to throw understandable messages that are part of your API contract and guide the client programmer.
* Throw early catch late - This principle implicitly says that you will throw an exception in the low level methods and make the exception climb the stack trace until you reached a sufficient level of abstraction to be able to handle the problem.
* Always cleanup after handling exception - If you are using resources like databases make sure you clean them up. You can use the new java 7 auto-cleanup via [try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) statement.
* Exception names must be clear and meaningful - Use for specific exceptions clear names that states the cause of the exception. For example `AccountLockedException` instead of `AccountEcxception`.
* Never use exceptions for flow control - it makes code hard to understand.
* Do not handle exceptions inside loops. Surround the loop with exception block instead.
* Always include all information about an exception in a single log message.
* Document all exceptions in your application in javadoc.



