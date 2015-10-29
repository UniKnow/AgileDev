# API Design

Languages for defining web services have been around for a while, with Web Application Description Language (WADL), which was born out of the SOAP WSDL era, representing a very technical approach to describe the REST APIs. While WADL is still in use, it hasn't seen the adaption many envisioned.

Recently new approaches emerged like [Swagger](http://swagger.io/), and [RAML](http://raml.org/). The initial vision behind Swagger was centered around documentation and client tooling but they quickly realized it was much bigger, and became about managing the overall lifecycle of APIs. The motivation behind RAML was about seeking one source of truth, and a basic representation of an API.

We need these new languages to describe and communicate around APIs. Without them we can't mockup, iterate and collaborate to find the best possible API design.


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
