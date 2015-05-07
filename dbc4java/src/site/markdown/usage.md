# Usage dbc4java

To use `dbc4java` within your project you need to include the following dependency within your `pom.xml`:

    <!-- Dependencies to implement Design by Contract -->
    <dependency>
        <groupId>org.uniknow.agiledev</groupId>
        <artifactId>dbc4java</artifactId>
        <version>${project.version}</version>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjrt</artifactId>
        <version>1.8.5</version>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.8.5</version>
    </dependency>
    <dependency>
        <groupId>cglib</groupId>
        <artifactId>cglib</artifactId>
        <version>1.8.5</version>
    </dependency>

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
To assure that the constraints are validated at every method invocation all classes need to be compiled with `aspectj`. For that the following snippet needs to be added to your `pom.xml`:

    <!--
    AspectJ compile time weaving. Required for Design by Contract
    -->
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>aspectj-maven-plugin</artifactId>
        <executions>
            <execution>
                <goals>
                    <goal>compile</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <source>${maven.compiler.source}</source>
            <target>${maven.compiler.target}</target>
            <showWeaveInfo>true</showWeaveInfo>
            <complianceLevel>${maven.compiler.target}</complianceLevel>

            <encoding>${project.build.sourceEncoding}</encoding>

            <weaveDependencies>
                <weaveDependency>
                    <groupId>org.uniknow.agiledev</groupId>
                    <artifactId>dbc4java</artifactId>
                </weaveDependency>
            </weaveDependencies>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjrt</artifactId>
                <version>${aspectj.version}</version>
            </dependency>
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjtools</artifactId>
                <version>${aspectj.version}</version>
            </dependency>
        </dependencies>
    </plugin>

*NOTE:* `dbc4java` has been tested with aspectj version `1.8.5`.

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

