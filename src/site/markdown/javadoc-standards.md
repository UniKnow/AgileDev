# JavaDoc standards

Javadoc is a key part of coding in Java. Within this chapter best practices regarding javadoc are shared:

## Document `public` and `protected` methods

All `public` and `protected` methods MUST be fully defined with javadoc. `package` and 'private` methods do not have to be, but may benefit from it.

If a method is overridden in a subclass, javadoc should only say something about what it distinct from the original definition of the method. The `@Override` annotation should be used to indicate to source code readers that teh javadoc is inherited in addition to its normal meaning.

## Use simple HTML tags, not valid XHTML

Javadoc uses HTML tags to identify paragraphs and other elements. Many developers get drawn to the thought that XHTML is better and ensure that all tags open and close correctly. This is a mistake. XHTML adds many extra tags that make the javadoc harder to read within the source code. The javadoc parser will interpret the incomplete HTML tags just fine.

## Use a single `<p>` tag between paragraphs

Longer javadoc always need multiple paragraphs. To seperate these from each other place a single `<p>` tag on the blank line between the paragraphs.

    /**
     * First paragraph.
     * <p>
     * Second paragraph.
     * May be on multiple lines.
     * <p>
     * Third paragraph.
     */
    public ...

## Use a single `<li>` tag for items in a list

Lists are useful in javadoc when explaining a set of options, choices or issues. Place in front of every item `<li>` at the start of the line and no closing tag. In order to get correct paragraph formatting, extra paragraph tags are required.

    /**
     * First paragraph.
     * <p><ul>
     * <li>the first item
     * <li>the second item
     * <li>the third item
     * </ul><p>
     * Second paragraph.
     */
    public ...

## Define a punchy first sentence

The first sentence, ended by a dot, is used in the next level higher javadoc. As such it has the responsibility of summing up the method or class. To achieve this the, first sentence should be clear and punchy, and generally short.

While not required, it is recommended that the first sentence is a paragraph itself. This helps retain the punchiness for the readers of the code.

## Use 'this' to refer to an instance of the class

When referring to an instance of the class being documented, use 'this' to reference it. For example `Returns a copy of this`.

## Aim for short single line sentences

Whenever possible, make javadoc sentences fit on a single line (80 till 120 characters). In most cases, each new sentence should start on a new line. This aids readability as source code, and simplifies refactoring re-writes of complex javadoc.

    /**
     * This is the first paragraph, on one line.
     * <p>
     * This is the first sentence of the second paragraph, on one line.
     * This is the second sentence of the second paragraph, on one line.
     * This is the third sentence of the second paragraph which is a bit longer so has been
     * split onto a second line, as that makes sense.
     * This is the fourth sentence, which starts a new line, even though there is space above.
     */
    public ...

## Use `@link` and `@code`

Many javadoc descriptions reference other methods and classes. This can be achieved most effectively using the `@link` and `@code` features.

The `@link` feature creates a visible hyperlink in generated Javadoc to the target. The `@link` target is one of the following forms:

    /**
     * First paragraph.
     * <p>
     * Link to a class named 'Foo': {@link Foo}.
     * Link to a method 'bar' on a class named 'Foo': {@link Foo#bar}.
     * Link to a method 'baz' on this class: {@link #baz}.
     * Link specifying text of the hyperlink after a space: {@link Foo the Foo class}.
     * Link to a method handling method overload {@link Foo#bar(String,int)}.
     */
    public ...

The `@code` feature provides a section of fixed-width font, ideal for references to methods and class names. While `@link` references are checked by the Javadoc compiler, `@code` references are not.

Only use `@link` on the first reference to a specific class or method. Use `@code` for subsequent references. This avoids excessive hyperlinks cluttering up the Javadoc.

## Never use `@link` in the first sentence

The first sentence is used in the higher level Javadoc. Adding a hyperlink in that first sentence makes the higher level documentation more confusing. Always use `@code` in the first sentence if necessary. `@link` can be used from the second sentence/paragraph onwards.

##  Do not use `@code` for `null`, `true` or `false`

The concepts of `null`, `true` and `false` are very common in Javadoc. Adding `@code` for every occurrence is a burden to both the reader and writer of the Javadoc and adds no real value.

## Use `@param`, `@return` and `@throws`

Almost all methods take in a parameter, return a result or both. The `@param` and `@return` features specify those inputs and outputs. The `@throws` feature specifies the thrown exceptions.

The `@param` entries should be specified in the same order as the parameters. The `@return` should be after the `@param` entries, followed by `@throws`.

## Use @param for generics

If a class or method has generic type parameters, then these should be documented. The correct approach is an `@param` tag with the parameter name of `<T>` where `T` is the type parameter name.

## Use one blank line before `@param`

There should be one blank line between the Javadoc text and the first `@param` or `@return`. This aids readability in source code.

## Treat `@throws` as an if clause

The `@throws` feature should normally be followed by "if" and the rest of the phrase describing the condition. For example, `@throws IllegalArgumentException if the file could not be found`. This aids readability in source code and when generated.

## Define null-handling for all parameters and return types

Whether a method accepts null on input, or can return `null` is critical information for building large systems. All non-primitive methods should define their null-tolerance in the `@param` or `@return`. Some standard forms expressing this should be used wherever possible:

* "not null" means that null is not accepted and passing in null will probably throw an exception , typically `NullPointerException`
* "may be null" means that null may be passed in. In general the behaviour of the passed in null should be defined
* "null treated as xxx" means that a null input is equivalent to the specified value
* "null returns xxx" means that a null input always returns the specified value

Other simple constraints may be added as well if applicable, for example "not empty, not null". Primitive values might specify their bounds, for example "from 1 to 5", or "not negative".

    /**
     * Javadoc text.
     *
     * @param foo  the foo parameter, not null
     * @param bar  the bar parameter, null returns null
     * @return the baz content, null if not processed
     */
    public String process(String foo, String bar) {...}

## Avoid `@author`

The `@author` feature can be used to record the authors of the class. This should be avoided, as it is usually out of date, and it can promote code ownership by an individual. The source control system is in a much better position to record authors.