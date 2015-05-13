# Best Practices REST API.

Some best practives for REST API design are implicit in the HTTP standard, while others have emerged over the past few years. This part presents a set of REST API best practices that should answer clear and concise questions like:

* How do i map non-CRUD operations to my URI.
* What is the appropriate HTTP response status code for a given scenario
* etc.

## URIs

REST APIs use Uniform Resource Identifiers (URIs) to address resources. The syntax of a URI is as follows

    URI = scheme "://" authority "/" path ["?" query] ["#" fragment]

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

Page 17 ...

## Resources

* [REST API Design Rulebook](http://ebookbrowsee.net/oreilly-rest-api-design-rulebook-oct-2011-pdf-d324669038)

