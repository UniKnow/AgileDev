# API Design

## Resource Definitions

Custom MIME types should be accompanied with both machine and human readable definition documents to allow for both dynamic client generation and easier developer understanding of the expected responses.

 **TODO:** Include guidelines on how this could be done.

## Swagger

To generate static documentation for the REST interfaces one could use swagger in combination with the following Maven plugin:

    <build>
        <plugins>
            <plugin>
                <groupId>com.github.kongchen</groupId>
                <artifactId>swagger-maven-plugin</artifactId>
                <version>${swagger-maven-plugin-version}</version>
                <configuration>
                    <apiSources>
                        <apiSource>
                            ...
                        </apiSource>
                    </apiSources>
                </configuration>
            </plugin>
        </plugins>
    </build>

### JAXRS

Annotate classes with `@Api` and the methods you want to expose with `@ApiOperation`.

### Spring MVC

TODO:
* Include that there must be `@RequestMapping` annotation at the class level of the rest controller to be picked up

The included annotations must be:

    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-annotations</artifactId>
        <version>${swagger.version}</version>
    </dependency>
