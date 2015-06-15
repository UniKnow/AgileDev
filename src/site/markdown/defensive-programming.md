# Defensive Programming

Defensive Programming is the practice of anticipating all possible ways that an end user could misuse an software system, and designing the system in such way that this is impossible, or to minimise the negative consequences. Goal of defensive programming is:

* Reducing the number of software bugs.
* Making software understandable; the source code should be readable and understandable and verified during code audit.
* Making the software behave in a predictable manner despite unexpected inputs or user actions.

Overly defensive programming could introduce code to prevent errors that can't happen, but needs to be executed on runtime and to be maintained by the developers. There is also the risk that the code catches or prevents too many exceptions. In those cases, the error would be suppressed and go unnoticed while the result would still be wrong.

Some defensive programming techniques are:

* Code reuse - Existing code is tested and known to work, reusing it may reduce the change of bugs being introduced.
* Legacy problems - Before reusing old libraries, APIs, and so forth, it must be validated whether the old work is valid for reuse. The library might for example have a much lower quality than the newly designed system.
* Low tolerance against 'potential' bugs - Assume that code constructs that appear to be problem prone (for example reported by a source code analyzer) are bugs and potentially security flaws.
* Encrypt/authenticate all data transmitted over networks. Do not implement your own encryption scheme, but use a proven one instead.
* [Design by Contract](dbc.md) - Use Design by Contract methodology to ensure that provided data (and the state of the program as a whole) is verified.
* Error codes - Prefer exceptions to return error codes, see [error handling](error-handling.md) for more details.

## See also:

* [PMD](http://pmd.sourceforge.net/) - 'Free' source code analyzer to detect potential bugs.


