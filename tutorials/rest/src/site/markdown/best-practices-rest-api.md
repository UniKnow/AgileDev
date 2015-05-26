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

### A query parameter should be used to support partial response.

A resource current state is represented by a set of fields and links. There may be times when a REST client offers a resource state representation that includes more than the client wishes to receive. In order to save on bandwidth a REST API can use a query parameter to trim the response data.
The query parameter allows clients to request only the resource state information that it deems relevant for its particular use case. The REST API must parse the requests query parameter inclusion list and return a partial response.
When a query parameter is used to define a partial response the media type must contain a parameter that list the fields that are included in the response.

### URIs should not be used to introduce new version of representation resource.

A resource representation may change over time but the identifier must consistently address the same resource. Therefore the version of a REST API, or any of its resources should not be represented in its URI.
A URI identifies a resource, independent of the version of its representation form and state. REST APIs should maintain a consistent mapping of its URIs to its domain constant resources. A REST API should only introduce a new URI if it intents to expose a new domain resource or functionality.

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

### `202` ("Accepted") must be used to indicate successful start of an asynchronous action.

A `202` response indicates that the client's request will be handled asynchronously. A `202` response is typically used for actions that take a long while to process.

Controller resources may send `202` responses, but other resource types should not.

### `204` ("No Content") should be used when the response body is intentionally empty.

The `204` status code is usually send back in response to a `PUT`, `POST`, or `DELETE` requests when the REST API declines to send back any status message or representation in the response message's body. An API may also send `204` as response to a `GET` request to indicate that the requested resource exist, but has no state representation to include in the body.

### `301` ("Moved Permanently") should be used to relocate resources.

The `301` status code indicates that the REST APIs resource model has been refactored and a new permanent URI has been assigned to the requested resource. The response should specify the new URI in the response's `Location` header.

### `302` ("Found") should not be used.

A status code of 302 tells a client that the resource they asked for has temporarily moved to a new location. The response should also include this location. It tells the client that it should carry on using the same URL to access this resource.
However the `302` response code has been misunderstood by programmers and incorrectly implemented in programs since version HTTP 1.0. In practice it was discovered that (A) led to a problem with submitted forms. Say you have a contact form, the visitor fills it and submits it and the client gets a 302 to a page saying "thanks, we'll get back to you". The form was sent using POST so the thanks page is also loaded using POST. Now suppose the visitor hits reload; the request is resent the same way it was obtained the first time, which is with a POST (and the same payload in the body). End result: the form gets submitted twice (and once more for every reload). Even if the client asks the user for confirmation before doing that, it's still annoying in most cases.
To clear things up, HTTP 1.1 introduced status code `303` ("See other") and `307` ("Temporary Redirect"), which should be used instead of `302`.

### `303` ("See other") should be used to refer the client to a different URI.

A `303` response indicates a controller resource that needs to return to the caller immediately but continue executing asynchronously (such as a long-lived image conversion), the REST API can provide a status check URI that allows the original client who requested the conversion to check on the conversion's status. This status check REST API should return `303` to the caller when the task is complete, along with a URI from which to retrieve the result in the `Location` HTTP header field.

### `304` ("Not modified") should be used to preserve bandwidth.

`304` is used when there is state information for the associated resource but the client has already the most recent version of the representation.

### `307` ("Temporary redirect") should be used to tell clients to resubmit the request to another URI.

A `307` response indicates that the REST API is not going to process the client's request. Instead the client should resubmit the request to the URI specified by the response message's `Location` header. A `307` response can be used to shift a client's request temporary over to another host.

### `400` ("Bad Request") may be used to indicate nonspecific failure.

`400` is the generic client side error status code used when no other `4xx` error code is appropriate.

### `401` (Unauthorized") must be used when there is a problem with the client's credentials.

A `401` error response indicates that the client tried to operate on a protected resource without providing the proper authorization.

### `403` ("Forbidden") should be used to forbid access regardless of authorization state.

A `403` indicates that the client's request is formed correctly but the REST API refuses to honor it. For example, a client may be authorized to interact with some, but not all resources. If the client attempts to access a resource outside of its permitted scope, the REST API should respond with `403`.
A `403` response is not a case of insufficient client credentials, that would result in a `401` ("Unauthorized").

### `404` ("Not found") must be used when a URI can't be mapped to a resource.

A `404`indicates that the client was able to communicate with a given server, but the server could not find what was requested.

### `405` ("Method not allowed") must be used when the HTTP method is not supported.

A `405` indicates that the client tried to use an HTTP method that the resource does not allow. A `405` response must include the `Allow` header, which lists the HTTP methods that the resource supports.

### `406` ("Not applicable") must be used when the requested media type cannot be served.

A `406` error response indicates that the API is not able to generate any of the client's preferred media types, as indicated by the `Accept` header.

### `409` ("Conflict") should be used to indicate violation of resource state.

A `409` error response tells the client that the original request would put the resource into a inconsistent state. Such a response may be returned when a client tries to delete a non-empty store resource.

### `412` ("Precondition failed") should be used to support conditional operations.

A client can specify one or more preconditions within the request headers that must be met. A `412` response indicates that those conditions were not met, so instead of carrying out the request, the REST API sends this status code.

### `415` ("Unsupported media type") must be used when the media type of the request payload cannot be processed.

The `415` response indicates that the API is not able to process the client's supplied media type as indicated by the `Content-Type` requests header.

### `500` ("Internal server error") should be used to indicate API malfunction.

`500` is the generic REST API error response which is usually returned by frameworks whenever request handler code is executed that raises an exception. A `500` error is never the client's fault and therefore it is reasonable for the client to retry the same request that triggered this response.

## HTTP Headers

This brief chapter suggests a set of best practices to work with HTTPs standard headers.

### `Content-Type` must be used.

The `Content-Type` header indicates the type of data found within the request/response message's body. The value of this header is a specially formatted string known as media type. Clients and servers rely on this header's value to tell how to parse a message's body.

### `Content-Length` should be used.

The `Content-Length` header gives the size of the body in bytes. By this a client can know whether it has read the correct number of bytes and a client can make a `HEAD` request to find out how large the body would be without downloading it.

### `Last-Modified` should be used in response.

The `Last-Modified` header applies to response messages only. The value is a timestamp that indicates the last time the state of a resource has been altered. Clients and cache may rely on this header to determine the freshness of their local copies. This header should always be supplied in response to `GET` requests.

### `ETag` should be used in response.

The value of `ETag` is an opaque string that identifies a specific version of a resource. The `ETag` value changes along with its resource state. This header should always be send in response to `GET` requests.
Clients may choose to save `ETag` headers for use in future `GET` requests, as the value of the conditional `If-None-Match` request header. If the server concludes that the resource hasn't changed, then it can save time and bandwidth by not sending the resource representation again, (and instead respond with `304` ("Not changed")).

### Stores must support conditional `PUT` requests.

A store uses the `PUT` method for both insert and update. A REST API must rely on the client to include the `If-Modified-Since` and `If-Match` to resolve any ambiguity. The `If-Modified-Since` request header asks the REST API to proceed with the operation only if the resource's state hasn't changed since the time indicated by the header's supplied time-stamp value. The `If-Match` header's value is an entity tag, which the client remember from an earlier response's `ETag` header's value. The `If-Match` header makes the request conditional based on a exact match of the provided value and the resource current entity tag value.

### Location must be used to specify the URI of a newly created resource.

The `Location` response header value contains a URI that identifies a resource that may be of interest to the client. In response to a successful creation of a resource within a store or collection, the REST API must include the `Location` to define the URI of the newly created resource.
In a `202` ("Accepted") response, the `Location` header may be used to direct clients to the status of an asynchronous controller resource.

### `Cache-Control`, `Expires` and `Date` response headers should be used to encourage caching.

To support legacy HTTP 1.0 caches, a REST API should include an `Expires` header with the expiration date-time. The response should also include a `Date` header, containing the date-time at which the API returned the response, so that clients can compute the freshness lifetime by determining the difference between `Expires` and `Date`.
The `Cache-Control` contains a `max-age` value in seconds which will be used within the cache as the freshness lifetime.

### Use `Cache-Control`, `Expires`, and `Pragma` to discourage caching.

If a response must not be cached, add `Cache-Control` headers with the value `no-cache` and `no-store`. Also add `Pragma: no-cache` and `Expires: 0` header values to support HTTP 1.0 caches.

### Caching should be encouraged.

The `no-cache` directive will prevent any caching. REST APIs should not do this unless absolutely necessary. Using a small value for `max-age` instead of `no-cache` is encouraged.

### Expiration caching headers should be used with `2xx` responses.

Expiration caching headers (`Cache-Control`, `Expires`, and `Date`) should be set in responses of successfull `GET` and `HEAD` requests. Although `POST` is cacheable, most caches treat this method as non cacheable. You need not set expiration on other methods.

### Expiration caching may optionally be used with `3xx` and `4xx` responses.

In additional to successful responses (response code `2xx`) consider adding caching headers to `3xx` and `4xx` responses. Caches which store these kind of results are known as negative cache, and helps to reduce the amount of redirecting and error triggering load on a REST API.

### HTTP headers must be optional.

Implement clients and servers such that they do not fail when they do not find expected custom headers.

## Media types

To identify the form of data that is communicated within a request or response message body, the `Content-Type` header value references a media type.
Media types have the following syntax:

    type "/" subtype*( ";" parameter)

The `type` may be one of:

* `application`
* `audio`
* `image`
* `message`
* `model`
* `multipart`
* `text`
* `video`

The `parameter` may follow *type/subtype* in the form of `attribute=value` pairs that are seperated by '`;`' character. a media type may  mark a parameter as either required or optional. Parameter names are case insensitive and values are normally case sensitive and may be enclosed in  '`"`'. Ordering of parameters is insignificant.

Media types use the subtype prefix `vnd` to indicate that they are owned (or controlled) by a vendor.

### Media type negotiation should be used to manage resource representation versioning.

The version of a resource representation should be managed via media types. The client uses media type negotiation to bind to the representational form that best suit their needs. See [introduction versioning](introduction#Versioning) for more details.

## Message body

A REST API commonly uses a message body to communicate the state of a resource. REST APIs commonly employ a text based format to represent a resource state as a set of meaningful fields. Today the most commonly used text formats are XML and JSON.

### JSON field names must be surrounded by double quotes.

The JSON syntax defines names as strings which are always surrounded by double quotes (`"`).

### JSON numbers should not be treated as Strings.

JSON supports number values directly, so they need not to be surrounded by double quotes.

### Use camelcase for JSON names.

JSON names should use camelcase, and avoid special characters whenever possible.

### Additional envelopes must not be created.

The body should contain only the representation of the resource state without any additional, transport oriented wrappers.

### A consistent form should be used to represent links.

A REST API response message body includes links to indicate the associations and actions that are available for a given resource. The representation of those links should be consistent. Preference is the Hypertext Application Language. Hypertext Application Language (HAL) is an Internet Draft (a "work in progress") standard convention for defining hypermedia such as links to external resources within JSON or XML code. The standard was initially proposed on June 2012 specifically for use with JSON and has since become available in two variations, each specific to JSON or XML. More details regarding this 'standard' can be found at [HAL specification](http://stateless.co/hal_specification.html).

### Minimize the number of advertised 'entry point' API URIs.

The REST API should provide human readable documentation that advertise the URI of the API docroot (to generate this kind of documentation have a look at [Swagger](http://swagger.io/)). The docroot's representation should provide links to make other resource programmatically available. From there the APIs hypermedia should guide the programmer.

### Links should be used to advertise resource's available actions in a state sensitive manner.

REST [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS#References) constraint specifies that an API must answer all client requests with resource representations that contain state sensitive links.

### A consistent form should be used to represents error responses.

A REST API could return error information in a message body when a request results in one or more errors. When using JSON a consistent form could be as follows

    {
        "errorCode": text,
        "errorDetail": test
    }

The unique `errorCode` should be used to understand what sort of error has occurred. A optional `errorDetail` could provide details specific to the request that caused the exception.


## Security

<TODO>