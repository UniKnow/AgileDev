# Versioning REST API

Always version your API. Versioning helps you iterate faster and prevents invalid requests from hitting updated endpoints. It also helps smooth over any major API version transitions as you can continue to offer old API versions for a period of time.

## Various forms of versioning

There are currently three common schools of thoughts regarding the versioning of REST APIs:

1. URL: You simply include the version into the URL, for example `http://localhost/api/V2/account`
2. Custom request header: You use a common URL (for example `http://localhost/api/account`) and add a header such as `api-version: 2`.
3. Accept header: You include the `Accept` header to specify the version, for example `Accept: application/vnd.agiledev.blog.v2+json`. If the header is missing you could return a `4xx` response.

There have been written a lot about these approaches and these will be listed in the paragraphs that describe the approaches in more detail, but here is the shortened version:

* Having version number in URL is bad because they should represent the entity.
* Custom request headers are bad because the HTTP spec gives us a means of requesting the nature we would like the resource represented by specifying the `Accept` header, why reproducing this?
* Accept headers are bad because they are harder to test. You can no longer just give someone a URL and say 'here click this'. Instead they have to carefully construct the request and configure the `Accept` header appropriately.

There are at least two redirection HTTP status codes that are appropriate for API versioning scenarios:

* `301` Moved permanently indicating that the resource with a requested URI is moved permanently to another URI. This status code can be used to indicate an obsolete/unsupported API version, informing API client that a versioned resource URI been replaced by a resource permalink.
* `302` Found indicating that the requested resource temporarily is located at another location, while requested URI may still supported. This status code may be useful when the version-less URIs are temporarily unavailable and that a request should be repeated using the redirection address (e.g. pointing to the URI with APi version embedded) and we want to tell clients to keep using it (i.e. the permalinks).
* other scenarios can be found in Redirection 3xx chapter of HTTP 1.1 specification

## Versioning by URL

By having per version a separate class/package with different `@Path` annotations:

    package com.abc.api.rest.v1.products;
    @Path("/rest/v1/products")
    public class ProductsResource {...}

    package com.abc.api.rest.v2.products;
    @Path("/rest/v2/products")
    public class ProductsResource {...}

Each class will have its version specific implementation. By having a base class containing the logic which doesn't change between versions we prevent that we have to duplicate code.

### Pro and Cons

**Pro**

* Allows browser caching.

**Con**

Embedding of API version into the URI would disrupt the concept of hypermedia as the engine of application state. For example, if API v3.0 is the latest API version, the following two should be aliases (i.e. behave identically to all API requests):

    http://shonzilla/api/customers/1234
    http://shonzilla/api/v3.0/customers/1234
    http://shonzilla/api/v3/customers/1234

In addition, API clients that still try to point to the old API should be informed to use the latest previous API version, if the API version they're using is obsolete or not supported anymore. So accessing any of the obsolete URIs like these:

    http://shonzilla/api/v2.2/customers/1234
    http://shonzilla/api/v2.0/customers/1234
    http://shonzilla/api/v2/customers/1234
    http://shonzilla/api/v1.1/customers/1234
    http://shonzilla/api/v1/customers/1234

Should return any of the `30x` HTTP status codes that indicate redirection that are used in conjunction with Location HTTP header that redirects to the appropriate version of resource URI which remain to be this one:

## Versioning by `Accept` header

Versioning of the REST service will be done via custom `Accept` headers containing custom media types (such as `application/vnd.agiledev.blog.v1+xml`) instead of using one of the defined media types, (see [RFC 4288 section 3.2](http://tools.ietf.org/html/rfc4288#section-3.2) for customisable MIME types).
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

### Pro and Cons

**Pro**

* The http accept header versioning allows you to version on each resource independently.

**Con**

* Putting a version number in an arbitrary header or the URL requires a layer 7 load balancer
* Poor support to write to `Accept` and `Content-Type` headers in HTML and JavaScript.
* URI is difficult to share. If you want to share the URI with version information with a college you would have to send instructions on what arguments to send to the server to get the right response.

<hr/>
**Include within paragraphs describing in more detail approach**

## Content Negotiation vs URI

There are a number of ways to version your API and within this paragraph we will discuss the most common ways.

* Versions in the URI:
* Content negotiation: A client specifies the type of resource they want back via the `Accepts` header, and that resource is versioned, (for example `Accepts: application/vnd.clientlist.v1+json`). If the header is missing you could return a 4xx response.

### Pro and Cons versions in the URI

**Con**

* If you have versions in your URL, then the same resource has two resource identifiers, for example `/V1/clients/1` and `/V2/clients/2`.

<hr/>
**TODO**

Describe how we can support the deprecation of REST API version

* Initially we want to warn the client of the REST API that the particular version of the service will become deprecated.
* Eventually we want to remove the complete version of the REST API. Should describe what is returned.

<hr/>