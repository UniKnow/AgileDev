# Validation Constraints

|Constraint|Description|Example|
|---|---|---|
|`@AssertFalse`|The value of the field must be `false`|`@AssertFalse`<br/>`boolean supported;`|
|`@AssertTrue`|The value of the field must be `true`|`@AssertTrue`<br/>`boolean supported;`|
|`@DecimalMax`|The value of the field or property must be a decimal value lower than or equal to the number in the value element.|`@DecimalMax("30.00")`<br/>`BigDecimal discount;`|
|`@DecimalMin`|The value of the field or property must be a decimal value greater than or equal to the number in the value element.|`@DecimalMin("5.00")`<br/>`BigDecimal discount;`|
