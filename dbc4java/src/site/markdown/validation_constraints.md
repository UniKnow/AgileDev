# Validation Constraints

The `dcb4spring` framework supports constraints in the form of annotations placed on a field, method or class of a spring managed bean.

The list below lists all the built-in constraints.

|Constraint|Description|Example|
|---|---|---|
|`@AssertFalse`|The value of the field or property must be `false`|`@AssertFalse`<br/>`boolean supported;`|
|`@AssertTrue`|The value of the field or property must be `true`|`@AssertTrue`<br/>`boolean supported;`|
|`@DecimalMax`|The value of the field or property must be a decimal value lower than or equal to the number in the value element. Supported types are `BigDecimal`, `BigInteger`, `String`, `byte`, `short`, `int`, `long`, and their respective wrappers. <br/> **Note** that `double` and `float` are not supported due to rounding errors |`@DecimalMax("30.00")`<br/>`BigDecimal discount;`|
|`@DecimalMin`|The value of the field or property must be a decimal value greater than or equal to the number in the value element. Supported types are `BigDecimal`, `BigInteger`, `String`, `byte`, `short`, `int`, `long`, and their respective wrappers. <br/> **Note** that `double` and `float` are not supported due to rounding errors |`@DecimalMin("5.00")`<br/>`BigDecimal discount;`|
|`@Digits`|The value of the field or property must be a number within a specified range. The `integer` element specifies the maximum integral digits for the number, and the `fraction` element specifies the maximum fractional digits for the number. Supported types are `BigDecimal`, `BigInteger`, `String`, `byte`, `short`, `int` and their respective wrapper types. `null` elements are considered valid.|`@Digits(interger=2, fraction=2)`<br/>`BigDecimal price;`|
|`@Email`|The value of the field or property must be a well-formed email address.|`@Email`<br/>`String emailAddress`|
|`@Future`|The value of the field or property must be a date in the future.|`@Future`<br/>`Date eventDate;`|
|`@Max`|The value of the field or property must be an integer value lower than or equal to the number in the value element|`@Max(10)`<br/>`int quantity;`|
|`@Min`|The value of the field or property must be an integer value greater than or equal to the number in the value element|`@Min(5)`<br/>`int quantity;`|
|`@NotBlank`|The value of the field or property must not be `null` or empty. The difference to `@NotEmpty` is that trailing whitespaces are getting ignored.|`@NotBlank`<br/>`String username;`|
|`@NotEmpty`|The value of the annotated `String`, `Collection`, `Map` or array must not be `null` or empty.|`@NotEmpty`<br/>`String username;`|
|`@NotNull`|The value of the field or property must not be `null`.|`@NotNull`<br/>`String userName;`|
|`@Null`|The value of the field or property must be `null`.|`@Null`<br/>`Object unusedValue`|
|`@Past`|The value of the field or property must be a date in the past|`@Past`<br/>`Date birthday;`|
|`@Pattern`|The value of the field or property must match the regular expression defined in the `regexp` element|`@Pattern(regexp="\\(\\d{3}\\)\\d{3}-\\d{4}")`<br/>`String phoneNumber;`|
|`@Size`|The size of the field or property is evaluated and must match the specified boundaries. If the field or property is `String`, the size of the string is evaluated. If the field or property is a `Collection`, `Map` or array, the size of the entity is evaluated. Use one of the optional `max` or `min` elements to specify the boundaries|`@Size(min=2, max=10)`<br/>`String identifier;`|
|`@URL`|THe value of the field or property must be a valid URL.|`@URL`<br/>`String url;`|

See [Using bean validation](http://docs.oracle.com/javaee/6/tutorial/doc/gircz.html)
