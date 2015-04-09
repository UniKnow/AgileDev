# Usage `dbc4java`

To use `dbc4java` within your project you need to include the following dependency within your project.


Classes for which its constraints need to be verified require to be annotated with `@Validated` and the constraints on fields, methods, etc are also added as annotations to the class/method.

    @Named
    @Validated
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public class PositiveInteger {

        @Min(0)
        private int value = 0;

        public void add(int addedValue) {
            value += addedValue;
        }

        public void subtract(int subtractedValue) {
            value -= subtractedValue;
        }

        public int toInt() {
            return value;
        }
    }

In the example above we assure by the constraint `@Min(0)` that the integer is always positive.

## Usage standalone

## Usage within Spring
To assure that the constraints are validated at every method invocation the dbc4java spring configuration file need to be imported.

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xsi:schemaLocation="
                http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context-3.0.xsd">

        <!-- Import dbc4spring configuration file -->
        <import resource="classpath:validation-spring-config.xml" />

        <context:component-scan base-package="org.uniknow.agiledev.dbc4spring"/>
    </beans>

