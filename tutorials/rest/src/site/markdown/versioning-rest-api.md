# Versioning REST API

Versioning of the REST service will be done via custom `Accept` headers containing custom media types (such as `application/vnd.agiledev.blog.v1+xml`) instead of using one of the defined media types.
Within the `BlogRestService` the custom consume and produce media types are defined

    /**
     * Version 1 of Blog Rest Service
     */
    package org.uniknow.agiledev.tutorial.rest.api.jaxrs.V1;

    @Path("/blog")
    @Consumes({ "application/agiledev.blog.v1+xml",
            "application/agiledev.blog.v1+json" })
    @Produces({ "application/agiledev.blog.v1+xml",
            "application/agiledev.blog.v1+json" })
    public interface BlogRestService {
        ...
    }

When the entities change in a non compatible way (V2), a new version of the API can co-exist with the first version, given that the entity/DTO namespace is also changed. The newer API will have its own interface and use an updated media type:

    /**
     * Version 2 of Blog Rest Service
     */
    package org.uniknow.agiledev.tutorial.rest.api.jaxrs.V2;

    @Path("/blog")
    @Consumes({"application/agiledev.blog.v2+xml", "application/agiledev.blog.v2+json" })
    @Produces({"application/agiledev.blog.v2+xml", "application/agiledev.blog.v2+json" })

and the namespace would become something like this (`package-info.java`):

    @XmlSchema(namespace = "http://www.uniknow.org/rest/blog/api/v2",
        elementFormDefault = XmlNsForm.QUALIFIED, xmlns = { @XmlNs(prefix = "api",
            namespaceURI = "http://www.uniknow.org/rest/blog/api/v2") })
    package org.uniknow.agiledev.tutorial.rest.api.jaxrs.V2;

    import javax.xml.bind.annotation.*;

Now the client is required to specify an `Accept` header when using the API. Whe can remedy this by adding default media types to the latest version of our API. This way clients invorking the API without an `Accept` header will be working with the latest version.

    @Path("/blog")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
            "application/agiledev.blog.v2+xml", "application/agiledev.blog.v2+json" })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
            "application/agiledev.blog.v2+xml", "application/agiledev.blog.v2+json" })

## Content Negotiation vs URI

There are a number of ways to version your API and within this paragraph we will discuss the most common ways.

* Versions in the URI:
* Content negotiation: A client specifies the type of resource they want back via the `Accepts` header, and that resource is versioned, (for example `Accepts: application/vnd.clientlist.v1+json`). If the header is missing you could return a 4xx response.

### Pro and Cons versions in the URI

**Con**

* If you have versions in your URL, then the same resource has two resource identifiers, for example `/V1/clients/1` and `/V2/clients/2`.

### Pro and Cons versioning by content negotiation

**Pro**

* The http accept header versioning allows you to version on each resource independently.

**Con**

* Putting a version number in an arbitrary header or the URL requires a layer 7 load balancer

<hr/>
**TODO**

Describe how we can support the deprecation of REST API version

* Initially we want to warn the client of the REST API that the particular version of the service will become deprecated.
* Eventually we want to remove the complete version of the REST API. Should describe what is returned.

<hr/>