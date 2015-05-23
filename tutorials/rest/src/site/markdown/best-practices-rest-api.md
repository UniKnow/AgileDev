# Best Practices REST API.

Some best practives for REST API design are implicit in the HTTP standard, while others have emerged over the past few years. This part presents a set of REST API best practices that should answer clear and concise questions like:

* How do i map non-CRUD operations to my URI.
* What is the appropriate HTTP response status code for a given scenario
* etc.

## Generic

### Always version your API

Versioning helps you iterate faster and prevents invalid requests from hitting updated endpoints. It also helps smooth over any major API version transitions as you can continue to offer old API versions for a period of time.

<TODO: extend with reference to best approach described here.>

## URIs

REST APIs use Uniform Resource Identifiers (URIs) to address resources. The syntax of a URI is as follows

    URI = scheme "://" authority "/" path ["?" query] ["#" fragment]

The query component of a URI contains a set of parameters to be interpreted as a variation of the resource that is identified by the path component.

### Indicate hierarchical relationships by forward slash.

The forward slash character (`/`) must be used in the path portion of the URI to indicate a hierarchical relationship between resources.

### Trailing forward slash should not be included in URIs.

A forward slash as the last character within a URIs path adds no semantic value. Every character within a URI counts within a resource's unique identity and two different URIs map to two different resources. Therefore REST APIs should not expect a trailing slash and should not include them in the links that they provide to clients.

### Hyphens should be used to improve readability of URIs

To make your URIs easy for people to scan and interpret, use the hyphen (`-`) character to improve the readability of names in long path segment. As rule of thumb, use the hyphen character where you would use a space.

### Underscores should not be used in URIs

Browsers often underline URIs to provide a visual cue that they are clickable. Depending on the application's font, the underscore character (`_`) can get partially obscured or completely hidden by this underlying. To avoid this confusion, use hyphens (`-`) instead of underscores.

See also ([Hyphens should be used to improve readability of URIs](#Hyphens-should-be-used-to-improve-readability-of-URIs)

### Lowercase letters should be preferred in URI paths

Lowercase letters are preferred in URI paths since RFC 3986 defines URIs as case sensitive expect for the scheme and host components. For example

    http://uniknow.github.io/AgileDev (1)
    http://uniknow.github.IO/AgileDev (2)
    http://uniknow.github.io/agileDev (3)

URI 1 and 2 are considered identical. URI 3 is not the same as URIs 1 and 2, which may cause unnecessary confusion.

### File extensions should not be included in URIs

A REST API should not include artificial file extensions in URIs to indicate the format of a message entity body. Instead, they should rely on the media type, as communicated through the `Content-Type` header.

### A singular noun should be used for document names

A URI representing a [document](resource-archetype.html) resource should be named with a singular noun.

For example, the URI for a single user document would have the following form:

    http://api.tutorial.rest.org/authors/mark

### A plural noun should be used for collection names

A URI identifying a [collection](resource-archetype.html) should be named with a plural noun and should be chose in such way that it reflects what it uniformly contains.

For example, the URI for a collection of authors would be as follows:

    http://api.tutorial.rest.org/authors

### A plural noun should be used for store names

A URI identifying a [store](resource-archetype.html) of resources should be named with a plural noun.

### A verb should be used for controller names

Like a programming function, a URI identifying a controller resource should be named in such way that the name indicates its action. For example:

    http://api.tutorial.rest.org/authors/mase/register

### CRUD function names should not be used in URIs

URIs should not be used to indicate that a CRUD function is performed. Instead the HTTP request methods (`GET`, `PUT`, `POST`, `DELETE`) should be used to indicate which CRUD method is performed.

### Query parameters of a URI may be used to filter collections or stores

URIs query parameters are natural fit for supplying search criteria to a [collection](resource-archetype.html) or [store](resource-archetype.html). For example:

    http://api.tutorial.rest.org/users?role=author

requests for a filtered list of all the users in the collection with a role `author`.

### Query parameters of a URI should be used to paginate collection or store results

Query parameters `pageSize` and `pageStartIndex` should be used to paginate [collection](resource-archetype.html) and [store](resource-archetype.html) results. The `pageSize` parameter specifies the maximum number of contained elements within the response. The `pageStartIndex` parameter specifies the zero-based index of the first element to return in the response. For example:

    http://api.tutorial.rest.org/users?pageSize=50&pageStartIndex=0

### Query parameters should be camel cased

<TODO>

## Request methods

### GET must be used to retrieve a representation of a resource

The `GET` method is used to retrieve the state of a resource in some representational form. A `GET` request may contain headers but no body.

### HEAD should be used to retrieve response headers

Clients use the `HEAD` method to retrieve the headers without a body. In other words, `HEAD` returns the same response as `GET`, but with an empty body. Clients can use this method to check whether a resource exists or to read its metadata.

Like `GET`, a `HEAD` request may contain headers but no body.

### PUT must be used to both insert and update a stored resource

`PUT` must be used to add a new resource to a [store](resource-archetype.html), with a URI specified by the client. `PUT` must also be used to update or replace an already stored resource.

The `PUT` request must include a representation of the resource that the client wants to store. However, the body of the request may, or may not be exactly the same as the client would receive from a `GET` request.

See also [POST vs PUT](post_vs_put.html).

### PUT must be used to update mutable resources

Clients must use the `PUT` method to make changes to resources. The `PUT` method may include a body that reflects the changes.

See also [POST vs PUT](post_vs_put.html).

### POST must be used to create a new resource in a collection

Clients use `POST` when attempting to create a new resource within a [collection](resource_archetypes.html). The `POST` body contains the suggested state representation of the new resource to be added to the server owned collection.

See also [POST vs PUT](post_vs_put.html).

### POST must be used to execute controllers

Clients use the `POST` method to invoke the function oriented [controller](resource_archetypes.html) resource. A `POST` request may include both headers and body as input to a controller resource. The `POST` method should NOT be used to get, store or delete resources.

`POST` methods are unsafe and non-idempotent, which means that its outcome is unpredictable and not guaranteed to be repeatable without potentially undesirable side effects.

### DELETE must be used to remove a resource from its parent

A client uses `DELETE` to request the removal of a resource from its parent, which is often a [collection](resource_archetype.html) or [store](resource_archetype.html). Once a `DELETE` request has been processed for a given resource, that resource can no longer be found by clients. Any future attempt to retrieve the resource, using either `GET` o `HEAD`, must result in an `404`("Not Found") response. If an API wishes to provide a 'soft' delete or some other state changing interaction, it should employ a special controller and direct its clients to use `POST` instead of `DELETE` to interact.

### OPTIONS should be used to retrieve metadata that described a resource's available interactions

Client may use the `OPTIONS` request method to retrieve resource metadata that includes an [`Allow`](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) header value. The response to a `OPTIONS` request may include a body that includes further details about each interaction option. For example, the response body could contain a list of links.

## Response status codes

HTTP defines standard status codes that can be used to convey the results of a client's request. Those status codes are divided in the following 5 categories:

|Category|Description|
|:------:|-----------|
|`1xx` Informational|Communicates transfer protocol-level information|
|`2xx` Success|Indicates that the client's request was accepted successfully|
|`3xx` Redirection|Indicates that the client must take some additional action in order to complete their request|
|`4xx` Client error|This category of error status codes indicates faults at client side|
|`5xx` Server error|This category of error status codes indicates faults at server side|

This section describes how and when to use the subset of status codes.

### `200` ("OK") should be used to indicate nonspecific success.

'200' indicates that the REST API successfully carried out whatever request the client requested, and that no mode specific code in the `2xx` series is appropriate. A `200` response should include a response body.

### `200` ("OK") must not be used to communicate errors in the response body.

Always use the proper HTTP response status codes and don't use the response body in a effort to accommodate less sophisticated HTTP clients.

### `201` ("Created") must be used to indicate successful resource creation.

A REST API responds with the `201` status code whenever a collection creates, a controller creates, or a store adds, a new resource.

### `202` ("Accepted") must be used to indicate successfull start of an asynchronous action.

A `202` response indicates that the client's request will be handled asynchronously. A `202` response is typically used for actions that take a long while to process.

Controller resources may send `202` responses, but other resource types should not.

## Resources

* [REST API Design Rulebook](http://ebookbrowsee.net/oreilly-rest-api-design-rulebook-oct-2011-pdf-d324669038)

