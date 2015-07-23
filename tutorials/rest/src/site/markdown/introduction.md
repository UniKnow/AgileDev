# RESTful Web services

Within this tutorial we will create a simple restful blogging system.

When providing services in a specific technology (jax-ws, jax-rs, ...), it's wise to avoid mixing the business logic of your service and the technology you are using to expose it to the outside world. This separation facilitates easier adaption of newer technologies and makes it easier to maintain and test services.

The project is separated in 2 packages.

1. api - defining the interfaces that are exposed
2. impl - Implementation of the interfaces

The `api` package contains the business interface `BlogService` within the package `org.uniknow.agiledev.tutorial.rest.api`, and the technology specific interface `BlogRestService` within the '`jaxrs` package containing JAX-RS annotations. The `jaxrs` package also contains the DTO (Data Transfer Objects) classes required to provide and/or consume the REST services.

## Running the example application

To build and run the application perform:

    mvn clean install cargo:run

Once the application is running the Rest services can be invoked by using for example the URL http://localhost:8080/rest/api/blog/posts and include header param `Accept` with for example value `application/agiledev.blog.v2+json`. If used without `Accept` header the response will be of content type `application/xml`.

To add a new message invoke a `POST` to the following URL http://localhost:8080/rest/api/blog/post with a body that matches the following. Make sure that the `Content-Type` is set to `application/xml`.

    <api:post xmlns:api="http://www.uniknow.org/rest/blog/api/v2">
        <title>Me again</title>
        <datePublished>2015-07-21T12:02:40.386+02:00</datePublished>
        <tags>example</tags>
        <categories>Java</categories>
        <categories>Tutorials</categories>
        <content>This is my second post.</content>
    </api:post>

